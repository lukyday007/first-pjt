import { Button } from "@components/ui/Button";
import { useNavigate } from "react-router-dom";

const GamePlay = () => {
  const navigate = useNavigate();

  return (
    <>
      <div>GamePlay Page</div>
      <div className="flex h-screen items-center justify-center bg-white">
        <Button onClick={() => navigate("/cam-chatting")} className="font-bold">
          화상채팅 시작
        </Button>
      </div>
    </>
  );
};

export default GamePlay;
