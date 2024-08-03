const useEndGame = () => {
  const removeSessionStorageList = [
    "gameStatus",
    "isAlive",
    "gameRoomId",
    "areaCenter",
    "areaRadius",
    "targetId",
  ];
  removeSessionStorageList.forEach(value => {
    sessionStorage.removeItem(value);
  });
};

export default useEndGame;
