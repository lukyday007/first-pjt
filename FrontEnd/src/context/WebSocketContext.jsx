import React, { createContext, useRef, useContext, useCallback } from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { GameContext } from "@/context/GameContext";

// WebSocket은 게임 방 접속시부터 실행되어야 함
// 게임 내(gameStatus===true)에서 WebSocket은 게임 상태(start-true, end-false), 영역 축소 신호만 수신 및 처리
export const WebSocketContext = createContext();

export const WebSocketProvider = ({ children }) => {
  const { gameRoomId, setGameStatus, setAreaRadius } = useContext(GameContext);
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
        break;
      case "alert":
        handleAlertDegree(alertDegree);
        break;
      case "end":
        setGameStatus(false);
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
