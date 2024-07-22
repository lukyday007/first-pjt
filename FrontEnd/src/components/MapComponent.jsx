import useGeolocation from '@/hooks/useGeolocation';
import useKakaoMap from '@/hooks/useKakaoMap';

const MapComponent = () => {
  // custom hooks
  const { isLoading, location, areaCenter, distance } = useGeolocation();
  // const mapRef = useKakaoMap(location, isLoading);
  const mapRef = useKakaoMap(location, isLoading, areaCenter);

  return isLoading ? (
    <div>
      <h1>Loading...</h1>
    </div>
  ) : (
    <div>
      <h3>Cnt: {areaCenter.lat}º {areaCenter.lng}º</h3>
      <h3>Loc: {location.lat}º {location.lng}º</h3>
      <h3>Distance: {distance}m</h3>

      <div
        id="map"
        ref={mapRef}
        style={{ width: '100%', height: '500px', border: '1px solid black' }}
      />
    </div>
  );
};

export default MapComponent;
