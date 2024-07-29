import React, { useContext } from "react";
import { GameContext } from "@/context/GameContext";
import useKakaoMap from "@/hooks/Map/useKakaoMap";
import crosshair from "@/assets/app-icon.png";

const MapComponent = () => {
  const { areaCenter, myLocation, distance, targetLocation } = useContext(GameContext);
  const { mapRef, panToMyLocation } = useKakaoMap();

  const handleCenter = () => {
    if (myLocation) {
      panToMyLocation(myLocation.lat, myLocation.lng);
    }
  }

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

      <div id="map-wrap" style={{ position: "relative" }}>
        <div
          id="map"
          ref={mapRef}
          style={{ width: "100%", height: "500px", border: "1px solid black", zIndex: 1 }}
        />
        <div id="map-control" style={{ position: "absolute", top: "10px", right: "10px", zIndex: 2 }}>
          <button
            style={{
              background: "white",
              border: "2px solid black",
              borderRadius: "20%",
              padding: "5px",
              cursor: "pointer",
              width: "40px",
              height: "40px",
            }}
            onClick={handleCenter}
          >
            <img src={crosshair} alt="Crosshair" style={{ width: "100%", height: "100%" }} />
          </button>
        </div>
      </div>

      {/* <div>
        <h3>Target Location:</h3>
        {targetLocation && (
          <div>
            Target Location: {targetLocation.lat}º {targetLocation.lng}º
          </div>
        )}
      </div> */}
    </div>
  );
};

export default MapComponent;
