import React, { useEffect, useContext, useState, useCallback } from "react";
import { useParams } from "react-router-dom";

import GameHeader from "@/components/GameHeader";
import MapComponent from "@/components/MapComponent";
import { GameContext } from "@/context/GameContext";

import useGameWebSocket from "@/hooks/WebSocket/useGameWebSocket";
import useStartGame from "@/hooks/Map/useStartGame";
import useSendGPS from "@/hooks/Map/useSendGPS";
import GameTime from "@/components/GameTime";
import useCatchTarget from "@/hooks/Map/useCatchTarget";

import {
  Carousel,
  CarouselContent,
  CarouselItem,
} from "@/components/ui/Carousel";
import { Button } from "@/components/ui/Button";
import GameRuleDialog from "@/components/GameRuleDialog";

import catchButton from "@/assets/gameplay-icon/catch-button.png";
import camchattingIcon from "@assets/material-icon/camchatting-icon.svg";
import itemIcon from "@assets/material-icon/item-icon.svg";
import giveUpIcon from "@/assets/material-icon/giveup-icon.svg";

//====================================================================

import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserVideoComponent from "@/hooks/WebRTC/UserVideoComponent";
import "../hooks/WebRTC/CamChatting.css";
import OvVideo from "@/hooks/WebRTC/OvVideo.jsx";

const APPLICATION_SERVER_URL =
  process.env.NODE_ENV === "production" ? "https://i11b205.p.ssafy.io/api" : "http://localhost:8080/";

let count = 1;
const GamePlay = () => {
  //===========================   GPS   ============================
  const { gameStatus } = useContext(GameContext);
  const { fetch, timeUntilStart } = useStartGame();
  const { startSendingGPS } = useSendGPS();
  const { isAbleToCatchTarget, handleCatchTarget } = useCatchTarget();
  const { connect, disconnect } = useGameWebSocket();

  const [camChatting, setCamChatting] = useState(false); // camChatting 상태 초기화
  const [isDialogOpen, setIsDialogOpen] = useState(false);

  const toggleCamChatting = () => {
    setCamChatting(prevState => !prevState); // camChatting 상태 토글 함수
  };

  useEffect(() => {
    connect();
    fetch();

    return () => {
      disconnect();
    };
  }, []);

  useEffect(() => {
    if (gameStatus) {
      const stopSendingGPS = startSendingGPS();
      return () => stopSendingGPS(); // 컴포넌트 unmount 시 GPS 전송 중지
    }
  }, [gameStatus]);

  //===========================   OPENVIDU   ============================

  const [session, setSession] = useState(undefined); // 방 생성 관련
  const [mainStreamManager, setMainStreamManager] = useState(undefined);
  const [publisher, setPublisher] = useState(undefined);
  const [subscribers, setSubscribers] = useState([]);
  const [currentVideoDevice, setCurrentVideoDevice] = useState(undefined);
  const audioEnabled = false;

  const username = localStorage.getItem("username"); // 추가
  const { gameRoomId: paramGameRoomId } = useParams(); // 추가

  useEffect(() => {
    const handleBeforeUnload = () => {
      leaveSession();
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [session]);

  const handleMainVideoStream = stream => {
    console.log("current main stream manager ", mainStreamManager);
    if (mainStreamManager !== stream) {
      setMainStreamManager(stream);
      console.log("=====> publisher : ", publisher);
      console.log("====> subscribers : ", subscribers);
    }
  };

  const handleButtonClick = async () => {
    console.log("you clicked ", count, " times!");

    count++;
  };

  const leaveSession = () => {
    if (session) {
      session.disconnect();
    }
    session(undefined);
    setSubscribers([]);
    setMainStreamManager(undefined);
    setPublisher(undefined);
  };

  const deleteSubscriber = streamManager => {
    setSubscribers(prevSubscribers =>
      prevSubscribers.filter(sub => sub !== streamManager)
    );
  };

  const joinSession = useCallback(async () => {
    const OV = new OpenVidu();

    OV.enableProdMode();

    console.log("joiSession");
    OV.isBrowserSupported = function () {
      return true;
    };

    OV.checkSystemRequirements = function () {
      return true;
    };

    // const newSession = OV.initSession(paramGameRoomId);

    // let newSession;

    // const OVSessionIdStored = localStorage.getItem("OVSessionID");
    // console.log("OVID:", OVSessionIdStored);

    // if (OVSessionIdStored && OVSessionIdStored != "undefined") {
    //   // OVSessionIdStored가 null 또는 빈 문자열이 아닌 경우 기존 세션을 사용
    //   console.log("기존 세션을 사용합니다.");
    //   newSession = OV.initSession(OVSessionIdStored);
    // } else {
    //   // OVSessionIdStored가 null 또는 빈 문자열일 때 새로운 세션을 생성
    //   console.log("OV Session 재생성");
    //   newSession = OV.initSession();
    //   console.log("newSession", newSession.options);
    //   localStorage.setItem("OVSessionID", newSession.sessionId);
    // }

    const newSession = OV.initSession();

    if (!session) {
      newSession.on("streamCreated", event => {
        const subscriber = newSession.subscribe(event.stream, undefined);
        setSubscribers(prevSubscribers => [...prevSubscribers, subscriber]);
        console.log("Updated subscribers: ", subscribers);
      });

      newSession.on("streamDestroyed", event => {
        deleteSubscriber(event.stream.streamManager);
      });

      newSession.on("exception", exception => {
        console.warn(exception);
        console.log("===============WARN!!!=============");
      });

      newSession.on("sessionDisconnected", async event => {
        console.log("=====> sessionDisconnected! ");
      });

      setSession(newSession);
    }

    const token = await getToken();

    try {
      await newSession.connect(token, { clientData: username });
      console.log("connectToken");
      // 권한 요청 부분 추가
      try {
        await navigator.mediaDevices.getUserMedia({
          video: true,
          audio: audioEnabled,
        }); // 권한 요청
        console.log("Permissions granted!");

        const newPublisher = await OV.initPublisherAsync(undefined, {
          audioSource: audioEnabled ? undefined : false,
          videoSource: undefined,
          publishAudio: audioEnabled,
          publishVideo: true,
          resolution: "640x480",
          frameRate: 30,
          insertMode: "APPEND",
          mirror: false,
        });

        await newSession.publish(newPublisher);

        const devices = await OV.getDevices();
        const videoDevices = devices.filter(
          device => device.kind === "videoinput"
        );
        const currentVideoDeviceId = newPublisher.stream
          .getMediaStream()
          .getVideoTracks()[0]
          .getSettings().deviceId;
        const currentVideoDevice = videoDevices.find(
          device => device.deviceId === currentVideoDeviceId
        );

        setCurrentVideoDevice(currentVideoDevice);
        setMainStreamManager(newPublisher);
        setPublisher(newPublisher);
      } catch (permissionError) {
        console.error("Permission denied:", permissionError);
        alert(
          "카메라 및 마이크 권한이 필요합니다. 브라우저 설정에서 권한을 허용해 주세요."
        );
      }
    } catch (error) {
      console.log(
        "There was an error connecting to the session:",
        error.code,
        error.message
      );
    }
  }, [username, session, subscribers]);

  useEffect(() => {
    const initSession = async () => {
      if (!session) {
        await joinSession();
      }
    };
    initSession();
  }, []);

  const switchCamera = useCallback(async () => {
    if (!currentVideoDevice || !session) return;

    try {
      const OV = new OpenVidu(); // switchCamera에서도 OV를 선언
      const devices = await OV.getDevices();
      const videoDevices = devices.filter(
        device => device.kind === "videoinput"
      );

      if (videoDevices.length > 1) {
        const newVideoDevice = videoDevices.find(
          device => device.deviceId !== currentVideoDevice.deviceId
        );

        if (newVideoDevice) {
          const newPublisher = OV.initPublisher(undefined, {
            videoSource: newVideoDevice.deviceId,
            publishAudio: true,
            publishVideo: true,
            mirror: true,
          });

          await session.unpublish(mainStreamManager);
          await session.publish(newPublisher);

          setCurrentVideoDevice(newVideoDevice);
          setMainStreamManager(newPublisher);
          setPublisher(newPublisher);
        }
      }
    } catch (e) {
      console.error(e);
    }
  }, [session, mainStreamManager, currentVideoDevice]);

  const getToken = async () => {
    const sessionId = await createSession(paramGameRoomId);
    return await createToken(sessionId);
  };

  const createSession = async sessionId => {
    const response = await axios.post(
      APPLICATION_SERVER_URL + "api/sessions",
      { customSessionId: sessionId },
      {
        headers: { "Content-Type": "application/json" },
      }
    );
    return response.data; // The sessionId
  };

  const createToken = async sessionId => {
    const response = await axios.post(
      APPLICATION_SERVER_URL + "api/sessions/" + sessionId + "/connections",
      {},
      {
        headers: { "Content-Type": "application/json" },
      }
    );
    return response.data; // The token
  };

  return (
    <>
      {timeUntilStart > 0 && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-75 text-3xl text-white">
          게임 시작까지 {Math.max(0, Math.ceil(timeUntilStart / 1000))}초
          남았습니다.
        </div>
      )}

      {/* publisher 의 카메라 인자 전달 */}
      <GameHeader
        publisher={publisher}
        handleMainVideoStream={handleMainVideoStream}
      />

      <div className="flex items-center justify-center">
        <div id="game-rule-dialog" className="m-4">
          <Button
            onClick={() => setIsDialogOpen(true)}
            className="h-[6vh] w-32 bg-gradient-to-r from-teal-400 to-blue-700 font-bold shadow-3d"
          >
            게임 규칙
          </Button>
          <GameRuleDialog
            isOpen={isDialogOpen}
            onClose={() => setIsDialogOpen(false)}
          />
        </div>
        <GameTime />
      </div>

      {/* 아래는 camChatting 상태에 따라 달라질 부분 (지도 <-> 화상)  */}

      {!camChatting ? (
        <>
          <MapComponent />
          <div className="item-center flex justify-center">
            <img
              src={catchButton}
              alt="catch-button"
              onClick={handleCatchTarget}
              className={`mr-4 h-[28vh] w-[28vh] ${isAbleToCatchTarget ? "" : "cursor-not-allowed opacity-40"}`}
            />
            <div id="mini-buttons" className="flex flex-col">
              <Button className="m-2 h-[7vh] w-[7vh] flex-col rounded-full border-black bg-gradient-to-r from-lime-200 to-teal-400 text-black">
                <img src={itemIcon} alt="item" />
                <div className="text-xs">아이템</div>
              </Button>
              <Button
                onClick={toggleCamChatting}
                className="m-2 h-[7vh] w-[7vh] flex-col rounded-full border-black bg-gradient-to-r from-cyan-200 to-indigo-400 text-black"
              >
                <img src={camchattingIcon} alt="chat" />
                <div className="text-xs">화상채팅</div>
              </Button>
              <button
                className={`m-2 flex h-[7vh] w-[7vh] items-center justify-center rounded-full bg-rose-500 transition-colors duration-300`}
              >
                <img src={giveUpIcon} alt="Give Up" className="w-12" />
              </button>
            </div>
          </div>
        </>
      ) : (
        <>
          <div className="relative h-[45vh] w-full rounded-lg bg-white">
            <Button
              onClick={() => setCamChatting(false)}
              className="border-1 absolute left-[3%] top-[3%] z-20 h-12 w-20 rounded-lg border-black bg-gradient-to-r from-emerald-300 to-emerald-500 font-bold text-white shadow-3d"
            >
              ← 지도
            </Button>
            {/* ==================   비디오 시작!  ===================*/}
            {session !== undefined ? (
              <div>
                {/* <h1 id="session-title">{mySessionId}</h1> */}
                {/* <div style={{overflow: "hidden"}}> */}
                {mainStreamManager === undefined ? (
                  <div id="main-video">
                    <UserVideoComponent streamManager={mainStreamManager} />
                  </div>
                ) : null}
                {/* </div> 
                <div>
                  {publisher === undefined ? (
                    <div 
                      onClick={() => handleMainVideoStream(publisher)}
                    >
                      <UserVideoComponent streamManager={publisher} />
                    </div>
                  ) : null}
                </div>
                */}

                <Carousel opts={{ align: "start" }}>
                  <CarouselContent>
                    {subscribers
                      .filter(sub => sub !== publisher) // publisher와 동일한 객체 필터링
                      .map((sub, index) => (
                        <CarouselItem key={sub.id} className="stream-container">
                          <UserVideoComponent
                            streamManager={sub}
                            currentUserNickname={username} // username 전달
                          />
                        </CarouselItem>
                      ))}
                  </CarouselContent>
                </Carousel>
                <div>
                  {subscribers.map((subscriber, idx) => {
                    const clientData = JSON.parse(
                      subscriber.stream.connection.data
                    ).clientData;
                    return (
                      <Button key={idx} onClick={handleButtonClick}>
                        {clientData}
                      </Button>
                    );
                  })}
                </div>
              </div>
            ) : null}
          </div>
        </>
      )}
    </>
  );
};

export default GamePlay;
