import { Button } from "@components/ui/Button";
import { useNavigate } from "react-router-dom";
import titleImage from "@assets/app-title.png";

const Home = () => {
  const navigate = useNavigate();
  return (
    <>
      <div>Home Page</div>
      <div className="m-4 flex h-screen flex-col items-center justify-center">
        <img
          src={titleImage}
          alt="titleImage"
          className="mb-8 aspect-square w-3/4"
        />
        <Button
          className="border-2 border-black bg-amber-400 font-bold text-black"
          onClick={() => navigate("/room")}
        >
          방 만들기
        </Button>
      </div>
    </>
  );
};

export default Home;
