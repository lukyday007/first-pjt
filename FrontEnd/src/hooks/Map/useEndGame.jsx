const useEndGame = () => {
  const removeSessionStorageList = [
    "gameStatus",
    "isLive",
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
