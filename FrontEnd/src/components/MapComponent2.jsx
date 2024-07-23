import useAreaCenter from '@/hooks/useAreaCenter';
import useCurrentLocation from '@/hooks/useCurrentLocation';
import useDistanceCalculator from '@/hooks/useDistanceCalculator';
import useKakaoMap2 from '@/hooks/useKakaoMap2';

const MapComponent = () => {
  const areaCenter = useAreaCenter();
  const { location, isLoading } = useCurrentLocation();
  const { distance } = useDistanceCalculator(location, areaCenter);
  const mapRef = useKakaoMap2(location, isLoading, areaCenter);

  return isLoading ? (
    <div>
      <h1>Loading...</h1>
    </div>
  ) : (
    <div>
      <h3>
        Cnt: {areaCenter.lat}º {areaCenter.lng}º
      </h3>
      <h3>
        Loc: {location.lat}º {location.lng}º
      </h3>
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
