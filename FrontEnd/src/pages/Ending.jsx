import React from "react";
import { Button } from "@/components/ui/Button";
import { useNavigate } from "react-router-dom";
import RankingTable from "@/components/RankingTable";

const Ending = () => {
  const navigate = useNavigate();

  return (
    <div className="flex flex-col items-center">
      <RankingTable />
      <Button onClick={() => navigate("/home")}>홈으로 돌아가기 ▶</Button>
    </div>
  );
};

export default Ending;
