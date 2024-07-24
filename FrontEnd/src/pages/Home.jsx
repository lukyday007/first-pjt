import { Button } from "@components/ui/Button";
import { useNavigate } from "react-router-dom";

import titleImage from "@assets/app-title.png";
import plusIcon from "@assets/material-icon/plus-icon.svg";
import qrcodeIcon from "@assets/material-icon/qrcode-icon.svg";
import trophyIcon from "@assets/material-icon/trophy-icon.svg";
import profileIcon from "@assets/material-icon/profile-icon.svg";

const Home = () => {
  const navigate = useNavigate();

  return (
    <div className="h-screen bg-theme-color-2">
      <div className="m-4 flex h-screen flex-col items-center justify-center">
        <img src={titleImage} alt="titleImage" className="mb-8 w-60" />
        <div className="grid grid-cols-2 gap-8">
          <div className="flex flex-col items-center">
            <Button
              className="mb-2 h-16 w-16 border-2 border-black bg-amber-300 font-bold text-black"
              onClick={() => navigate("/room")}
            >
              <img src={plusIcon} alt="plus" />
            </Button>
            <span className="font-bold">방 만들기</span>
          </div>

          <div className="flex flex-col items-center">
            <Button
              className="mb-2 h-16 w-16 border-2 border-black bg-teal-300 font-bold text-black"
              onClick={() => navigate("/qrcode")}
            >
              <img src={qrcodeIcon} alt="qrcode" />
            </Button>
            <span className="font-bold">방 코드 찍기</span>
          </div>

          <div className="flex flex-col items-center">
            <Button
              className="mb-2 h-16 w-16 border-2 border-black bg-rose-300 font-bold text-black"
              onClick={() => navigate("/rank")}
            >
              <img src={trophyIcon} alt="rank" />
            </Button>
            <span className="font-bold">순위</span>
          </div>

          <div className="flex flex-col items-center">
            <Button
              className="mb-2 h-16 w-16 border-2 border-black bg-indigo-300 font-bold text-black"
              onClick={() => navigate("/profile")}
            >
              <img src={profileIcon} alt="profile" />
            </Button>
            <span className="font-bold">내 정보</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
