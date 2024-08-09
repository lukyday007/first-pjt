import React, { useEffect, useContext, useState, useCallback } from "react";
import { useParams } from "react-router-dom";

import GameHeader from "@/components/GameHeader";
import MapComponent from "@/components/MapComponent";
import CamChattingComponent from "@/components/CamChattingComponent";

import { GameContext } from "@/context/GameContext";
import useFirebase from "@/hooks/Map/useFirebase";
import useTimer from "@/hooks/Map/useTimer";
import PlotGameTime from "@/components/PlotGameTime";
import CatchTargetButton from "@/components/CatchTargetButton";
import useCatchTarget from "@/hooks/Map/useCatchTarget";
import CheckMyItemButton from "@/components/CheckMyItemButton";
import CamChattingButton from "@/components/CamChattingButton";


//====================================================================


import React, { useState, useEffect, useCallback } from 'react';
import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import UserVideoComponent from "@/hooks/WebRTC/UserVideoComponent";
import "../hooks/WebRTC/CamChatting.css";
import OvVideo from "@/hooks/WebRTC/OvVideo.jsx";

import {
  Carousel,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from '@/components/ui/Carousel.jsx';

const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'https://demos.openvidu.io/';


//====================================================================


const GamePlay = () => {
  const { gameRoomId: paramGameRoomId } = useParams();
  const {
    setGameRoomId,  // 게임 룸 아이디
    gameStatus,
    myLocation,
    areaRadius,
    distance,
    username,       // 유저 이름 
  } = useContext(GameContext);
  
  const { sendGPS } = useFirebase();
  const { decreaseTime } = useTimer();
  const { isAbleToCatchTarget, handleOnClickCatchTarget } = useCatchTarget();
  const [camChatting, setCamChatting] = useState(false); // camChatting 상태 초기화

  useEffect(() => {
    setGameRoomId(paramGameRoomId);
  }, [paramGameRoomId, setGameRoomId]);

  useEffect(() => {
    if (gameStatus && myLocation && distance !== null && areaRadius !== null) {
      const locationInterval = setInterval(() => {
        sendGPS(username, myLocation.lat, myLocation.lng); // 1초마다 위치 전송

        if (distance > areaRadius) {
          decreaseTime(); // 1초마다 영역 이탈 여부 체크해 시간 감소
        }
      }, 1000);

      return () => clearInterval(locationInterval);
    }
  }, [
    gameStatus,
    myLocation,
    distance,
    areaRadius,
    sendGPS,
    username,
    decreaseTime,
  ]);

  const toggleCamChatting = () => {
    setCamChatting(prevState => !prevState); // camChatting 상태 토글 함수
  };

//===========================   OPENVIDU   ============================

  const [session, setSession] = useState(undefined);
  const [mainStreamManager, setMainStreamManager] = useState(undefined);
  const [publisher, setPublisher] = useState(undefined);
  const [subscribers, setSubscribers] = useState([]);
  const [currentVideoDevice, setCurrentVideoDevice] = useState(undefined);
  const audioEnabled = false;


  useEffect(() => {
    const handleBeforeUnload = () => {
      leaveSession();
    };

    window.addEventListener('beforeunload', handleBeforeUnload);

    return () => {
      window.removeEventListener('beforeunload', handleBeforeUnload);
    };
  }, [session]);

  const handleMainVideoStream = (stream) => {
    if (mainStreamManager !== stream) {
      setMainStreamManager(stream);
    }
  };

  const deleteSubscriber = (streamManager) => {
    setSubscribers((prevSubscribers) => prevSubscribers.filter(sub => sub !== streamManager));
  };

  const joinSession = useCallback(async () => {
    const OV = new OpenVidu();

    console.log("joiSession")
    // 브라우저 감지 우회
    OV.isBrowserSupported = function() {
        return true; // 무조건 브라우저가 지원되는 것으로 간주
    };

    OV.checkSystemRequirements = function() {
        return true; // 시스템 요구사항을 무시하고 통과시키기
    };

    const newSession = OV.initSession();

    // 이벤트 리스너 중복 등록 방지를 위한 확인
    if (!session) {
      newSession.on('streamCreated', (event) => {
        const subscriber = newSession.subscribe(event.stream, undefined);
        setSubscribers((prevSubscribers) => [...prevSubscribers, subscriber]);
        console.log("Updated subscribers: ", subscribers);
      });

      newSession.on('streamDestroyed', (event) => {
        deleteSubscriber(event.stream.streamManager);
      });

      newSession.on('exception', (exception) => {
        console.warn(exception);
      });

      setSession(newSession); // session 상태를 업데이트
    }

    const token = await getToken();

    try {
      await newSession.connect(token, { clientData: username });

      const newPublisher = await OV.initPublisherAsync(undefined, {
        audioSource: undefined,
        videoSource: undefined,
        publishAudio: false,
        publishVideo: true,
        resolution: '640x480',
        frameRate: 30,
        insertMode: 'APPEND',
        mirror: false,
      });

      await newSession.publish(newPublisher);

      const devices = await OV.getDevices();
      const videoDevices = devices.filter(device => device.kind === 'videoinput');
      const currentVideoDeviceId = newPublisher.stream.getMediaStream().getVideoTracks()[0].getSettings().deviceId;
      const currentVideoDevice = videoDevices.find(device => device.deviceId === currentVideoDeviceId);

      setCurrentVideoDevice(currentVideoDevice);
      setMainStreamManager(newPublisher);
      setPublisher(newPublisher);

    } catch (error) {
      console.log('There was an error connecting to the session:', error.code, error.message);
    }
  }, [username, session, subscribers]);


  useEffect(() => {
    const initSession = async () => {
      if (!session) {
        await joinSession();
      }
    }
    initSession();
  }, []);


  // const leaveSession = useCallback(() => {
  //   if (session) {
  //       session.disconnect();
  //   }

  //   setSession(undefined);
  //   setSubscribers([]);
  //   setMainStreamManager(undefined);
  //   setPublisher(undefined);
  // }, [session, gameRoomId, username]);


  const switchCamera = useCallback(async () => {
    if (!currentVideoDevice || !session) return;

    try {
      const OV = new OpenVidu(); // switchCamera에서도 OV를 선언
      const devices = await OV.getDevices();
      const videoDevices = devices.filter(device => device.kind === 'videoinput');

      if (videoDevices.length > 1) {
        const newVideoDevice = videoDevices.find(device => device.deviceId !== currentVideoDevice.deviceId);

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

  const createSession = async (sessionId) => {
    const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
        headers: { 'Content-Type': 'application/json' },
    });
    return response.data; // The sessionId
  };

  const createToken = async (sessionId) => {
    const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
        headers: { 'Content-Type': 'application/json' },
    });
    return response.data; // The token
  };


  return (
    <>
      <GameHeader />
      {camChatting ? (
        <>
          <PlotGameTime />
          <div>
            {/* ==================   비디오 시작!  ===================*/}
            {session !== undefined ? (
              <div id="session">            
                <h1 id="session-title">{mySessionId}</h1>
                {/* <div style={{overflow: "hidden"}}> */}
                  {mainStreamManager === undefined ? (
                    <div id="main-video" className="col-md-6">
                      <UserVideoComponent streamManager={mainStreamManager} />
                    </div>
                  ) : null}
                {/* </div> */}
                {/* <div>
                  {publisher === undefined ? (
                    <div 
                      onClick={() => handleMainVideoStream(publisher)}
                    >
                      <UserVideoComponent streamManager={publisher} />
                    </div>
                  ) : null}
                </div> */}

                <Carousel opts={{ align: "start" }}>
                  <CarouselContent>
                    {subscribers.map((sub, index) => (
                      <CarouselItem key={sub.id} className="stream-container col-md-6 col-xs-6">
                        <div onClick={() => handleMainVideoStream(sub)}>
                          <span>{sub.id}</span>
                          <UserVideoComponent streamManager={sub} />
                        </div>
                      </CarouselItem>
                    ))}
                  </CarouselContent>
                </Carousel> 

              </div>
            ) : null}
          </div>
        </>
      ) : (
        <>
          <MapComponent />
          <PlotGameTime />
          <div className="flex justify-between">
            <div />
            <div />
            <div id="catch-button" className="flex justify-center">
              <CatchTargetButton
                onClick={handleOnClickCatchTarget}
                isDisabled={!isAbleToCatchTarget}
              />
            </div>
            <div />
            <div id="mini-buttons" className="mx-3 flex flex-col">
              <CheckMyItemButton />
              <CamChattingButton onClick={toggleCamChatting} /> 
              <GiveUpButton />
            </div>
            <div />
          </div>
        </>
      )}
    </>
  );
};

export default GamePlay;
