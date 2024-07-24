import { useEffect, useState } from 'react'; // React 훅을 가져옵니다.
import webSocketService from '@/services/WebSocketService'; // WebSocketService를 가져옵니다.

const useWebSocket = (roomId, location) => {
    const [otherLocations, setOtherLocations] = useState([]); // 다른 플레이어들의 위치 정보를 저장할 상태 변수입니다.

    // useEffect 훅을 사용하여 컴포넌트가 마운트될 때와 언마운트될 때 특정 작업을 수행합니다.
    useEffect(() => {
        // 서버에서 수신한 메시지를 처리하는 함수입니다.
        const handleReceivedMessage = (message) => {
          switch (message.type) {
            case "Location List":
              // 기존의 위치 정보 배열에 새로운 위치 정보를 추가합니다.
              setOtherLocations((prevLocations) => [...prevLocations, message]);
            case "Game Started":
              console.log("Game Started");
            case 1:
              console.log(1);
            case 2:
              console.log(2);
            case 3:
              console.log(3);
            case "Game ended":
              console.log("Game ended");
          }
        };

        // WebSocketService를 사용하여 서버에 연결하고 메시지를 구독합니다.
        webSocketService.connect(roomId, handleReceivedMessage);
        
        // 30초마다 위치 정보를 전송하는 interval을 설정합니다.
        const locationInterval = setInterval(() => {
            if (location) {
                webSocketService.sendLocation(roomId, location); // 위치 정보를 서버로 전송합니다.
            }
        }, 30000); // 30초 (30000ms)

        // 컴포넌트가 언마운트될 때 WebSocket 연결을 해제하고 interval을 정리합니다.
        return () => {
            webSocketService.disconnect();
            clearInterval(locationInterval); // interval을 해제합니다.
        };
    }, [roomId, location]); // roomId와 location이 변경될 때마다 useEffect가 재실행됩니다.

    return { otherLocations }; // 다른 플레이어들의 위치 정보를 반환합니다.
};

export default useWebSocket;
