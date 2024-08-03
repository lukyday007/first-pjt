import { useState, useEffect, useContext } from "react";
import { GameContext } from "@/context/GameContext";
import axiosInstance from "@/api/axiosInstance";

const DISTANCE_TO_CATCH = 5; // 잡기 버튼이 활성화되기 위한 타겟과의 거리

const useCatchTarget = () => {
  const { gameRoomId, distToTarget } = useContext(GameContext);
  const [isAbleToCatchTarget, setIsAbleToCatchTarget] = useState(false);

  useEffect(() => {
    if (distToTarget) {
      setIsAbleToCatchTarget(distToTarget < DISTANCE_TO_CATCH);
    }
  }, [distToTarget]);

  const handleOnClickCatchTarget = async () => {
    try {
      const response = await axiosInstance.patch(
        `/in-game/${gameRoomId}/catchTarget`
      );
      if (response.status === 200) {
        console.log("Target caught successfully!");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return { isAbleToCatchTarget, handleOnClickCatchTarget };
};

export default useCatchTarget;
