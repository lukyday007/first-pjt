import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import { Button } from "@components/ui/Button";
import { Input } from "@components/ui/Input";

import { BASE_URL } from "@/constants/baseURL";
import kaKaoLoginImg from "@/assets/kakao-login.png";
import googleLoginImg from "@/assets/google-login.svg";

const Login = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async () => {
    try {
      const response = await axios.post(`${BASE_URL}/user/login`, {
        email,
        password,
      });

      if (response.status == 200) {
        const { accessToken, refreshToken, username, email } = response.data;
        localStorage.setItem("accessToken", accessToken);
        localStorage.setItem("refreshToken", refreshToken);
        localStorage.setItem("username", username);
        localStorage.setItem("email", email);

        navigate("/home");
      } else {
        setError("로그인에 실패했습니다. 다시 시도해주세요.");
      }
    } catch (err) {
      setError(
        "서버와 통신하는 중에 문제가 발생했습니다. 나중에 다시 시도해주세요."
      );
    }
  };

  const handleKakaoLogin = () => {
    const REST_API_KEY = ""; // env에서 import 해야함
    const REDIRECT_URI = "http://localhost:5080/auth/kakao";

    // 카카오 서버에 로그인 요청
    // - 카카오에서 요청 처리 후 인가코드와 함께 KakaoLogin 페이지로 리다이렉트
    window.location.href = `https://kauth.kakao.com/oauth/authorize?client_id=${REST_API_KEY}&redirect_uri=${REDIRECT_URI}&response_type=code`;
  };

  const handleGoogleLogin = () => {
    // 구글 로그인
  };

  return (
    <div className="m-4 flex h-screen flex-col items-center justify-center">
      <h1 className="mb-4 p-4 text-2xl font-bold">로그인</h1>
      <div className="w-60">
        <Input
          type="email"
          placeholder="email"
          className="mb-4 p-4"
          value={email}
          onChange={e => setEmail(e.target.value)}
        />
        <Input
          type="password"
          placeholder="password"
          className="mb-8 p-4"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />
      </div>
      <Button
        type="submit"
        className="mb-8 w-60 bg-theme-color-1 font-bold"
        onClick={handleLogin}
      >
        Login &gt;
      </Button>
      <div className="mb-8 cursor-pointer" onClick={() => navigate("/signup")}>
        계정이 없으신가요?{" "}
        <span className="font-bold text-rose-500">회원가입</span>
      </div>

      {/* 구분선 */}
      <div className="mb-8 flex w-full items-center">
        <div className="border-lightGray/30 flex-grow border-t"></div>
        <span className="mx-4 text-gray-400">또는</span>
        <div className="border-lightGray/30 flex-grow border-t"></div>
      </div>

      <img
        src={kaKaoLoginImg}
        alt="Kakao Login"
        className="mb-4 w-60 cursor-pointer"
        onClick={handleKakaoLogin}
      />
      <img
        src={googleLoginImg}
        alt="Google Login"
        className="w-60 cursor-pointer"
        onClick={handleGoogleLogin}
      />
      {error && <div className="text-red-500">{error}</div>}
    </div>
  );
};

export default Login;
