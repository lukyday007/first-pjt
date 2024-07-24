import { Button } from "@components/ui/Button";
import { Input } from "@components/ui/Input";

const Signup = () => {
  const handleSignup = () => {
    // 회원가입 로직 추가해야 함
  };

  return (
    <div className="m-4 flex h-screen flex-col items-center justify-center">
      <h1 className="mb-4 p-4 text-2xl font-bold">회원가입</h1>
      <div className="w-60">
        <Input type="text" placeholder="nickname" className="mb-4 p-4" />
        <Input type="email" placeholder="email" className="mb-4 p-4" />
        <Input type="password" placeholder="password" className="mb-8 p-4" />
      </div>
      <Button
        type="submit"
        className="bg-theme-color-2 mb-8 font-bold text-cyan-600"
        onClick={handleSignup}
      >
        Sign Up &gt;
      </Button>
    </div>
  );
};

export default Signup;
