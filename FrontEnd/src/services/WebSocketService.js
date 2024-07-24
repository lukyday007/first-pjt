import SockJS from 'sockjs-client'; // SockJS 라이브러리를 가져옵니다.
import Stomp from 'stompjs'; // STOMP 프로토콜을 사용하기 위한 라이브러리를 가져옵니다.

class WebSocketService {
    constructor() {
        this.stompClient = null; // STOMP 클라이언트를 저장할 변수입니다.
    }

    // WebSocket 연결을 설정하고 STOMP 클라이언트를 초기화하는 메서드입니다.
    connect(roomId, onMessageReceived) {
        const socket = new SockJS('/server'); // '/server' 엔드포인트로 SockJS 객체를 생성합니다.
        this.stompClient = Stomp.over(socket); // SockJS 객체를 통해 STOMP 클라이언트를 생성합니다.
        this.stompClient.connect({}, (frame) => { // 서버와 연결을 시도합니다.
            console.log('Connected: ' + frame);
            // 특정 토픽을 구독합니다. 여기서는 방(room)의 위치 정보를 구독합니다.
            this.stompClient.subscribe(`/topic/room/${roomId}/location`, (msg) => {
                const message = JSON.parse(msg.body); // 수신한 메시지를 JSON 형태로 파싱합니다.
                onMessageReceived(message); // 콜백 함수로 메시지를 전달합니다.
            });
        });
    }

    // 서버로 위치 정보를 전송하는 메서드입니다.
    sendLocation(roomId, location) {
        if (this.stompClient && this.stompClient.connected) {
            // STOMP 클라이언트를 통해 서버로 메시지를 전송합니다.
            this.stompClient.send(`/app/room/${roomId}/location`, {}, JSON.stringify(location));
        }
    }

    // WebSocket 연결을 해제하는 메서드입니다.
    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect(); // STOMP 클라이언트를 통해 연결을 해제합니다.
        }
        console.log("Disconnected");
    }
}

// WebSocketService 인스턴스를 생성하여 기본적으로 export 합니다.
const webSocketService = new WebSocketService();
export default webSocketService;
