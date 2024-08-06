import { useEffect, useRef, useContext } from "react";
import { GameContext } from "@/context/GameContext";
import useFirebase from "@/hooks/Map/useFirebase";
import useTimer from "@/hooks/Map/useTimer";

const useSendGPS = () => {
  const { gameStatus, myLocation, distance, areaRadius, username } =
    useContext(GameContext);
  const { sendGPS } = useFirebase();
  const { decreaseTime } = useTimer();

  const locationRef = useRef(myLocation);
  const distanceRef = useRef(distance);

  useEffect(() => {
    locationRef.current = myLocation;
    distanceRef.current = distance;
  }, [myLocation]);

  useEffect(() => {
    if (gameStatus && locationRef.current) {
      const locationInterval = setInterval(() => {
        sendGPS(username, myLocation.lat, myLocation.lng); // 1초마다 위치 전송

        if (distanceRef.current > areaRadius) {
          decreaseTime(); // 1초마다 영역 이탈 여부 체크해 시간 감소
        }
      }, 1000);

      return () => clearInterval(locationInterval);
    }
  }, [gameStatus, sendGPS, username, decreaseTime]);
};

export default useSendGPS;
