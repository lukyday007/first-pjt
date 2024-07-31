import React, {
  createContext,
  useRef,
  useEffect,
  useContext,
  useCallback,
} from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import axiosInstance from "@/api/axiosInstance.js";
import { GameContext } from "@/context/GameContext";

// WebSocket은 게임 방 접속시부터 실행되어야 함
// 게임 내(gameStatus===true)에서 WebSocket은 게임 상태(start-true, end-false), 영역 축소 신호만 수신 및 처리
export const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  const { userId, gameRoomId, setGameStatus, setTargetId, setAreaCenter, setAreaRadius } =
    useContext(GameContext);
  const stompClient = useRef(null);

  // connect, disconnect 함수 정의
  const connect = useCallback(() => {
    if (!gameRoomId) return;

    const socket = new SockJS("/server");
    stompClient.current = Stomp.over(socket);
    stompClient.current.connect({}, frame => {
      console.log("Connected: " + frame);

      stompClient.current.subscribe(`/topic/alert/${gameRoomId}`, serverMsg => {
        const msg = JSON.parse(serverMsg.body);
        handleAlertMessage(msg);
      });
    });
  }, [gameRoomId]);

  const disconnect = useCallback(() => {
    if (stompClient.current) {
      stompClient.current.disconnect();
      console.log("Disconnected");
    }
  }, []);

  const handleAlertMessage = msg => {
    const { msgType, alertDegree } = msg;
    switch (msgType) {
      case "start":
        setGameStatus(true);
        // gameStatus가 true로 변동 시, Room.jsx에서 GamePlay.jsx로 navigate하도록 함
        break;
      case "alert":
        handleAlertDegree(alertDegree);
        break;
      case "end":
        setGameStatus(false);
        break;
      case "ready":
        // gameroom/${gameRoomId}/startInfo로 요청
        try {
          const response = await axiosInstance.get(`/gameroom/${gameRoomId}/startInfo`);
          if (response.data.success) {
            // 반경, 중심, 타겟 닉네임 수신
            setAreaRadius(response.data.mapSize);
            setAreaCenter({
              lat: parseFloat(response.data.centerLat).toFixed(5),
              lng: parseFloat(response.data.centerLng).toFixed(5),
            })
            setTargetId(response.data.targetName);
          } else {
            alert("게임 시작 관련 정보를 가져오는 중 오류가 발생했습니다.");
          }
        } catch (err) {
          alert("서버와 통신하는 중에 문제가 발생했습니다. 나중에 다시 시도해주세요.");
        }

        // in-game/${gameRoomId}/assignMissions로 요청
        try {
          const response = await axiosInstance.get(`/in-game/${gameRoomId}/assignMissions`);
          if (response.data.success) {
            // MissionResponse 관련 로직 작성
          } else {
            alert("할당된 미션 수신 과정에서 오류가 발생했습니다.");
          }
        } catch (err) {
          alert("서버와 통신하는 중에 문제가 발생했습니다. 나중에 다시 시도해주세요.");
        }
        break;
      case "target":
        // 타겟이 죽거나 나가서 변동사항 발생 시
        const hunter = msg.hunter;
        if (hunter === userId) {
          setTargetId(msg.target);
        }
        break;
      default:
        break;
    }
  };

  const handleAlertDegree = degree => {
    switch (degree) {
      case 1:
      case 2:
      case 3:
        setAreaRadius(prev => prev * 0.75); // 임의로 설정
        break;
      case 4:
        setGameStatus(false);
        break;
      default:
        break;
    }
  };

  // /room, /game-play에서만 import해서 작동하도록
  // 여기 context와 Room.jsx에만 connect(), disconnect() 활용
  useEffect(() => {
    connect();

    return () => {
      disconnect();
    };
  }, [connect, disconnect]);

  return (
    <WebSocketContext.Provider value={{ connect, disconnect }}>
      {children}
    </WebSocketContext.Provider>
  );
};
