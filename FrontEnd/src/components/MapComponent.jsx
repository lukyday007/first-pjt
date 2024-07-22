import useGeolocation from '@/hooks/useGeolocation';
import useKakaoMap from '@/hooks/useKakaoMap';

const MapComponent = () => {
  // custom hooks
  const { isLoading, location, areaCenter, distance } = useGeolocation();
  const mapRef = useKakaoMap(location, isLoading);

  return isLoading ? (
    <div>
      <h1>Loading...</h1>
    </div>
  ) : (
    <div>
      <h3>Lat: {location.lat} deg</h3>
      <h3>Lng: {location.lng} deg</h3>

      <h3>Center</h3>
      <h3>Lat: {areaCenter.lat} deg</h3>
      <h3>Lng: {areaCenter.lng} deg</h3>

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
