import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { BASE_URL } from "@/constants/baseURL";

const KakaoLogin = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const urlParams = new URL(window.location.toString()).searchParams;
    const code = urlParams.get("code");
    const error = urlParams.get("error");
    const state = urlParams.get("state");

    if (!error) {
      // 로그인 실패, 로그인 페이지로 리다이렉트
      navigate("/login");
      return;
    }

    const data = { code, state };

    axios
      .post(`${BASE_URL}/kakao/login`, data) // 카카오서버에서 받은 인가코드를 백엔드 서버로 전송
      .then(resp => {
        if (resp.status === 6000) {
          // 카카오 로그인은 완료했으나 새로 가입해야하는 유저 -> 닉네임 설정페이지로 리다이렉트
          const email = resp.data.email; // 이거 다음페이지까지 넘겨야 함
          sessionStorage.setItem("email", email);
          navigate("/kakao/signup");
        } else if (resp.status === 404) {
          // 다른 사람이 url 가로채서 로그인 하려 한 것, 인증되지 않은 유저
          navigate("/login");
        } else if (resp.status === 200) {
          // 로그인 성공
          navigate("/home");
        } else if (resp.status === 400) {
          // 오류
          navigate("/login");
        }
      })
      .catch(() => {
        navigate("/login");
      });
  }, [navigate]);

  return null;
};

export default KakaoLogin;
