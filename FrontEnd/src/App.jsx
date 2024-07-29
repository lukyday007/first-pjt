import React from "react";
import { Routes, Route } from "react-router-dom";
import { GameProvider } from "@/context/GameContext";
import Onboarding from "./pages/Onboarding";
import Login from "./pages/Login";
import Signup from "./pages/Signup";
import Home from "./pages/Home";
import Room from "./pages/Room";
import GamePlay from "./pages/GamePlay";
import CamChatting from "./pages/CamChatting";
import PrivateChatting from "./pages/PrivateChatting";

const App = () => {
  return (
    <GameProvider>
      <Routes>
        <Route path="/" element={<Onboarding />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/home" element={<Home />} />
        <Route path="/room" element={<Room />} />
        <Route path="/game-play" element={<GamePlay />} />
        <Route path="/cam-chatting" element={<CamChatting />} />
        <Route path="/private-chatting" element={<PrivateChatting />} />
      </Routes>
    </GameProvider>
  );
};

export default App;
