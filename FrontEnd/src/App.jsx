import { Routes, Route } from 'react-router-dom';
import OnBoarding from './pages/OnBoarding';
import Login from './pages/Login';
import Home from './pages/Home';
import Room from './pages/Room';

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<OnBoarding />} />
      <Route path="/login" element={<Login />} />
      <Route path="/home" element={<Home />} />
      <Route path="/room" element={<Room />} />
    </Routes>
  );
};

export default App;
