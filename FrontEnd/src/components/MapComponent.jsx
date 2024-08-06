import React from "react";
import useKakaoMap from "@/hooks/Map/useKakaoMap";
import MapCenterButton from "./MapCenterButton";

const MapComponent = () => {
  const { mapRef, panToMyLocation } = useKakaoMap();

  const handleOnClickCenter = () => {
    panToMyLocation(myLocation.lat, myLocation.lng);
  };

  return (
    <div className="flex justify-center">
      <div className="w-full p-2">
        <div id="map-wrap" className="relative">
          <div
            id="map"
            ref={mapRef}
            className="z-10 h-[45vh] w-full rounded-lg border-2 border-black"
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
