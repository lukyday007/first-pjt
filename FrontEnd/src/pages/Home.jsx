import { Button } from '@components/ui/Button';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const navigate = useNavigate();
  return (
    <>
      <div className="flex h-screen items-center justify-center bg-white">
        <Button onClick={() => navigate('/room')}>방 만들기</Button>
      </div>
    </>
  );
};

export default Home;
