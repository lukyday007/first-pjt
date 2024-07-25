import { useEffect, useRef, useContext } from "react";
import { GameContext } from "@/context/GameContext";
import SockJS from "sockjs-client";
import Stomp from "stompjs";

// WebSocket 연결 및 메시지 처리를 관리하는 커스텀 hook
const useWebSocket = () => {
  const {
    gameRoomId, // 게임 방 정보
    targetId, // 타겟 정보
    setGameStatus, // 게임 시작 시(게임 서버와 WebSocket 연결 시) gameStatus를 변경
    setTargetLocation, // 서버로부터 지속적으로 타겟의 위치 정보 가져옴
    setAreaCenter, // 게임 시작 시, 방장 - 중심 위치 정보를 서버에 보냄, 기타 - 중심 위치 정보를 서버로부터 받음
    setAreaRadius, // 게임 시작 시, 방에서 지정한 반경 정보를 받음
  } = useContext(GameContext);
  const stompClient = useRef(null);

  useEffect(() => {
    if (!gameRoomId || !setGameStatus) return;

    const socket = new SockJS("/server");
    stompClient.current = Stomp.over(socket);
    stompClient.current.connect({}, frame => {
      console.log("Connected: " + frame);

      // subscribe
      stompClient.current.subscribe(`/topic/alert/${gameRoomId}`, serverMsg => {
        const msg = JSON.parse(serverMsg.body);
        handleAlertMessage(msg);
      });

      // 자신이 잡아야 할 target이 있다면, target의 위치 정보가 서버로부터 전송되면 세팅
      if (targetId) {
        stompClient.current.subscribe(
          `/topic/location/${targetId}`,
          serverMsg => {
            const targetLoc = JSON.parse(serverMsg.body);
            setTargetLocation({
              lat: targetLoc.lat,
              lng: targetLoc.lng,
            });
          }
        );
      }
    });

    return () => {
      if (stompClient.current) {
        stompClient.current.disconnect();
      }
    };
  }, [gameRoomId, targetId]);

  // subscribe msgType에 따라 실행 동작 분기
  const handleAlertMessage = msg => {
    const { msgType, alertDegree } = msg;
    switch (msgType) {
      case "start":
        // 여기서 현재 WebSocket 연결한 유저 게임 참여자 컨트롤러의 DB insert method로 axios 요청
        // .then => setGameStatus, setMyLocation, setTargetLocation, setAreaCenter, setAreaRadius
        setGameStatus(true);
        break;
      case "alert":
        handleAlertDegree(alertDegree);
        break;
      case "end": // 게임 승리 조건 만족으로 인한 게임 조기종료
        setGameStatus(false);
        break;
      default:
        break;
    }
  };

  // msgType = 1, 2, 3일 때에 따라 반경 변동
  const handleAlertDegree = degree => {
    switch (degree) {
      case 1:
        console.log("25% 경과");
        // setAreaRadius();
        break;
      case 2:
        console.log("50% 경과");
        // setAreaRadius();
        break;
      case 3:
        console.log("75% 경과");
        // setAreaRadius();
        break;
      case 4:
        // 게임 시간 종료로 인한 게임 종료
        setGameStatus(false);
        break;
      default:
        break;
    }
  };

  // publisher 동작 부분
  // 현재 위치를 서버에 전송하는 부분, MapComponent.jsx에서 동작
  const sendLocation = location => {
    if (stompClient.current) {
      stompClient.current.send("/pub/location", {}, JSON.stringify(location));
    }
  };

  return { sendLocation };
};

export default useWebSocket;
