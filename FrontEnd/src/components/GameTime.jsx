import { useContext } from "react";
import { GameContext } from "@/context/GameContext";
import useStartGame from "@/hooks/Map/useStartGame";
import React, { useState, useEffect, useRef } from "react";

const GameTime = () => {
  const { setGameStatus, setIsAlive } = useContext(GameContext);
  // const { startTime, gamePlayTime } = useStartGame();
  console.log(
    `sessionStorage - startTime: ${sessionStorage.getItem("startTime")}`
  );
  console.log(
    `sessionStorage - gamePlayTime: ${sessionStorage.getItem("gamePlayTime")}`
  );

  const startTime = sessionStorage.getItem("startTime");
  const gamePlayTime = parseInt(sessionStorage.getItem("gamePlayTime"));
  console.log(`GameTime.jsx - startTime: ${startTime}`);
  console.log(`GameTime.jsx - gamePlayTime: ${gamePlayTime}`);

  // 남은 시간 초기화
  const initializeRemainingPlayTime = () => {
    const storedRemainingTime = sessionStorage.getItem("remainingPlayTime");
    return storedRemainingTime
      ? parseInt(storedRemainingTime, 10)
      : gamePlayTime;
  };

  // 남은 시간 상태 관리
  const [remainingPlayTime, setRemainingPlayTime] = useState(
    initializeRemainingPlayTime
  );
  const intervalIdRef = useRef(null);

  // 화면에 표시하는 시간 포맷 함수
  const formatTime = seconds => {
    const minutes = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${minutes < 10 ? "0" : ""}${minutes}:${secs < 10 ? "0" : ""}${secs}`;
  };

  // 타이머 업데이트 함수
  const updateTimer = () => {
    const start = new Date(startTime).getTime();
    const now = Date.now();
    const elapsedTime = Math.floor((now - start) / 1000); // 경과 시간
    const newRemainingPlayTime = gamePlayTime - elapsedTime; // 남은 시간
    console.log(`GameTime.jsx - updateTimer - start: ${start}`);
    console.log(`GameTime.jsx - updateTimer - now: ${now}`);

    // 대기시간 1분 경과 시 gameStatus, isAlive 변경
    if (elapsedTime >= 60) {
      setGameStatus(true);
      setIsAlive(true);
      sessionStorage.setItem("gameStatus", true);
      sessionStorage.setItem("isAlive", true);
    }

    if (newRemainingPlayTime <= 0) {
      // 남은 시간이 0이 되면 타이머 정지하고 남은 시간을 0으로 설정
      clearInterval(intervalIdRef.current);
      if (remainingPlayTime !== 0) {
        setRemainingPlayTime(0);
        sessionStorage.setItem("remainingPlayTime", "0");
      }
    } else {
      // 남은 시간을 sessionStorage에 저장
      setRemainingPlayTime(newRemainingPlayTime);
      sessionStorage.setItem(
        "remainingPlayTime",
        newRemainingPlayTime.toString()
      );
    }
  };

  useEffect(() => {
    if (!startTime || !gamePlayTime) return; // 시작 시간이 없으면 타이머를 설정하지 않음

    updateTimer(); // 초기 타이머 업데이트

    intervalIdRef.current = setInterval(updateTimer, 1000); // 매초 타이머 업데이트

    return () => clearInterval(intervalIdRef.current); // 컴포넌트 unmount 시 interval 정리
  }, [gamePlayTime, startTime]);

  return (
    <div className="m-4 flex h-16 w-48 items-center justify-center rounded-lg border-2 border-black bg-white text-4xl font-bold text-black">
      ⏱ {formatTime(remainingPlayTime)}
    </div>
  );
};

export default GameTime;
