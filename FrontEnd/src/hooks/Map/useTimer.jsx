import { useState, useEffect, useCallback, useContext } from "react";
import { GameContext } from "@/context/GameContext";
import axiosInstance from "@/api/axiosInstance.js";

const useTimer = initialTime => {
  const { gameRoomId, setGameStatus, username } = useContext(GameContext);
  const [time, setTime] = useState(() => {
    const savedTime = localStorage.getItem("remainingTime");
    return savedTime !== null ? parseInt(savedTime, 10) : initialTime; // localStorage에 시간 정보가 있으면 사용
  });

  const decreaseTime = useCallback(() => {
    setTime(prevTime => prevTime - 1);
  }, []);

  useEffect(() => {
    if (time <= 0) {
      // 주어진 시간을 모두 소진 시 사망 처리 및 axios
      (async () => {
        try {
          const response = await axiosInstance.patch(
            `/participants/${gameRoomId}/${username}/die`
          );
          if (response.status == 200) {
            // 타이머 종료로 인한 사망 후 처리 부분 입력
            setGameStatus(false);
          } else {
            // 타이머 종료 상태를 서버에 보냈으나 실패 시 부분 입력
          }
        } catch {
          // axios 요청 실패 시 부분 입력
        }
      })();
    }

    localStorage.setItem("remainingTime", time);
  }, [time, gameRoomId, username]);

  return { time, decreaseTime };
};

export default useTimer;
