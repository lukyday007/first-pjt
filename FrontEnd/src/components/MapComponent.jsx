import React, { useContext } from "react";
import { GameContext } from "@/context/GameContext";
import useKakaoMap from "@/hooks/Map/useKakaoMap";

const MapComponent = () => {
  const { areaCenter, areaRadius, myLocation, targetLocation, distance } =
    useContext(GameContext);
  const mapRef = useKakaoMap(myLocation, areaCenter, areaRadius);

  return !myLocation ? (
    <div>
      <h1>Loading...</h1>
    </div>
  ) : (
    <div>
      <h3>
        Cnt: {areaCenter.lat}º {areaCenter.lng}º
      </h3>
      <h3>
        Loc: {myLocation.lat}º {myLocation.lng}º
      </h3>
      <h3>Distance: {distance}m</h3>

      <div
        id="map"
        ref={mapRef}
        style={{ width: "100%", height: "500px", border: "1px solid black" }}
      />

      <div>
        <h3>Target Location:</h3>
        {targetLocation && (
          <div>
            Target Location: {targetLocation.lat}º {targetLocation.lng}º
          </div>
        )}
      </div>
    </div>
  );
};

export default MapComponent;
