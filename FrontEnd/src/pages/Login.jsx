import { useNavigate } from "react-router-dom";
import { Button } from "@components/ui/Button";
import { Input } from "@components/ui/Input";

const Login = () => {
  const navigate = useNavigate();

  const handleLogin = () => {
    // 로그인 인증 로직 추가해야 함
    navigate("/home");
  };

  return (
    <div className="m-4 flex h-screen flex-col items-center justify-center">
      <h1 className="mb-4 p-4 text-2xl font-bold">로그인</h1>
      <div className="w-60">
        <Input type="email" placeholder="email" className="mb-4 p-4" />
        <Input type="password" placeholder="password" className="mb-8 p-4" />
      </div>
      <Button
        type="submit"
        className="bg-theme-color-1 mb-8 font-bold"
        onClick={handleLogin}
      >
        Login &gt;
      </Button>
      <div className="cursor-pointer" onClick={() => navigate("/signup")}>
        계정이 없으신가요?{" "}
        <span className="font-bold text-rose-500">회원가입</span>
      </div>
    </div>
  );
};

export default Login;
