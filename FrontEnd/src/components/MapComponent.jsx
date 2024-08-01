import React, { useContext } from "react";
import { GameContext } from "@/context/GameContext";
import useKakaoMap from "@/hooks/Map/useKakaoMap";
import { MapCenterButton } from "@/components/MapCenterButton";

const MapComponent = () => {
  const { areaCenter, myLocation, distance, targetLocation } =
    useContext(GameContext);
  const { mapRef, panToMyLocation } = useKakaoMap();

  const handleCenter = () => {
    if (myLocation) {
      panToMyLocation(myLocation.lat, myLocation.lng);
    }
  };

  const styles = {
    mapWrap: {
      position: "relative",
    },
    map: {
      width: "100%",
      height: "500px",
      border: "1px solid black",
      zIndex: 1,
    },
    mapControl: {
      position: "absolute",
      top: "1%",
      right: "1%",
      zIndex: 2,
    },
  };

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

      <div id="map-wrap" style={styles.mapWrap}>
        <div id="map" ref={mapRef} style={styles.map} />
        <div id="map-control" style={styles.mapControl}>
          <MapCenterButton onClick={handleCenter} />
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
