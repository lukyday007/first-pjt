import { useState, useEffect } from 'react';

const useAreaCenter = () => {
  const [areaCenter, setAreaCenter] = useState({ lat: 0, lng: 0 });

  useEffect(() => {
    if (navigator.geolocation) {
      const success = position => {
        const { latitude, longitude } = position.coords;
        setAreaCenter({
          lat: latitude,
          lng: longitude,
        });
      };
      const error = err => {
        console.error('Error getting location:', err);
      };
      navigator.geolocation.getCurrentPosition(success, error);
    } else {
      console.log('Geolocation is not supported by this browser.');
    }
  }, []);

  return areaCenter;
};

export default useAreaCenter;
