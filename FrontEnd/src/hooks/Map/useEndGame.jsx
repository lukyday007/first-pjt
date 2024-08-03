const useEndGame = () => {
  const removeSessionStorageList = [
    "gameStatus",
    "isAlive",
    "gameRoomId",
    "areaCenter",
    "areaRadius",
    "targetId",
    "remainingTime",
  ];
  removeSessionStorageList.forEach(value => {
    sessionStorage.removeItem(value);
  });
};

export default useEndGame;
