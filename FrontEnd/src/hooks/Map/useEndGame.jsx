const useEndGame = () => {
  sessionStorage.removeItem("gameRoomId");
  sessionStorage.removeItem("areaCenter");
  sessionStorage.removeItem("areaRadius");
  sessionStorage.removeItem("targetId");
};

export default useEndGame;
