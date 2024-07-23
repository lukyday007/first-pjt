import { Button } from "@components/ui/Button";
import { useNavigate } from "react-router-dom";

const Room = () => {
  const navigate = useNavigate();

  return (
    <>
      <div>Room Page</div>
      <div className="flex h-screen items-center justify-center bg-white">
        <Button
          onClick={() => navigate("/game-play")}
          className="bg-theme-color-1 font-bold"
        >
          게임 시작
        </Button>
      </div>
    </>
  );
};

export default Room;
