import { useState, useContext } from "react";
import { GameContext } from "@/context/GameContext";
import axiosInstance from "@/api/axiosInstance.js";

const useStartGame = () => {
  const { myLocation, gameRoomId } = useContext(GameContext);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const handleStartGame = async () => {
    try {
      await axiosInstance.post(`/gameroom/${gameRoomId}/start`, {
        centerLat: myLocation.lat,
        centerLng: myLocation.lng,
      });

      // 요청에서 에러가 없었다면 로딩 상태로 변경
      setIsLoading(true);
    } catch (err) {
      setError(
        "서버와 통신하는 중에 문제가 발생했습니다. 나중에 다시 시도해주세요."
      );
    }
  };

  return { handleStartGame, isLoading, error };
};

export default useStartGame;
