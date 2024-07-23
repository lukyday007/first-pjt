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
      <div>
        <Input type="email" placeholder="email" className="mb-4 p-4" />
        <Input type="password" placeholder="password" className="mb-8 p-4" />
      </div>
      <Button
        type="submit"
        className="bg-theme-color-1 font-bold"
        onClick={handleLogin}
      >
        로그인
      </Button>
    </div>
  );
};

export default Login;
