import { useNavigate } from 'react-router-dom';
import { Button } from '@components/ui/Button';
import onBoardingImage from '@assets/onboarding-image.png';

const OnBoarding = () => {
  const navigate = useNavigate();
  return (
    <div className="m-4 flex h-screen flex-col items-center justify-center">
      <img
        src={onBoardingImage}
        alt="onBoardingImage"
        className="mb-8 aspect-square w-3/4"
      />
      <h1 className="mb-2 p-4 text-2xl font-bold">
        BORICORI에 오신 것을 환영합니다
      </h1>
      <p className="mb-8">보리꼬리! 꼬리 한 번 잡아볼까요?</p>
      <Button
        onClick={() => navigate('/login')}
        className="bg-emerald-500 font-bold"
      >
        Get Started
      </Button>
    </div>
  );
};

export default OnBoarding;
