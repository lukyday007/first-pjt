import React, { useContext } from "react";
import { GameContext } from "@/context/GameContext";
import useKakaoMap from "@/hooks/Map/useKakaoMap";
import MapCenterButton from "./MapCenterButton";

const MapComponent = () => {
  const { areaCenter, myLocation, distance, targetLocation } =
    useContext(GameContext);
  const { mapRef, panToMyLocation } = useKakaoMap();

  const handleOnClickCenter = () => {
    if (myLocation) {
      panToMyLocation(myLocation.lat, myLocation.lng);
    }
  };

  return !myLocation ? (
    <div>
      <h1>Loading...</h1>
    </div>
  ) : (
    <div className="flex min-h-screen justify-center">
      <div className="w-full max-w-screen-lg p-2">
        <h3>
          Cnt: {areaCenter.lat}º {areaCenter.lng}º
        </h3>
        <h3>
          Loc: {myLocation.lat}º {myLocation.lng}º
        </h3>
        <h3>Distance: {distance}m</h3>

        <div id="map-wrap" className="relative">
          <div
            id="map"
            ref={mapRef}
            className="z-10 h-[50vh] w-full rounded-lg border border-black"
          />
          <div id="map-control">
            <MapCenterButton onClick={handleOnClickCenter} />
          </div>
        </div>
      </div>
    </div>
  );
};

export default MapComponent;
