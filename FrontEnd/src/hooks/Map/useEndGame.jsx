import { useNavigate } from "react-router-dom";

const useEndGame = () => {
  const navigate = useNavigate();
  const gameRoomId = sessionStorage.getItem("gameRoomId");

  // gameRoomUsers는 Ending.jsx에서 활용 후 remove
  const removeSessionStorageList = [
    "gameStatus",
    "isAlive",
    "gameRoomId",
    "areaCenter",
    "areaRadius",
    "targetId",
    "remainingTime",
    "startTime",
    "gamePlayTime",
    "remainingPlayTime",
  ];

  const removeSessionStorageFunc = () => {
    removeSessionStorageList.forEach(value => {
      sessionStorage.removeItem(value);
    });
  };

  removeSessionStorageFunc();

  navigate(`/ending/${gameRoomId}`);
};

export default useEndGame;
