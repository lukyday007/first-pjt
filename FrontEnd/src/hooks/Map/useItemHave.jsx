import { useState, useContext } from "react";
import { GameContext } from "@/context/GameContext";

const useItemHave = () => {
  const { itemList, setItemList } = useContext(GameContext);

  // 1. 스텔스 망토 havingBlockScreen
  // 2. 방해 폭탄 havingBlockGPS
  // 3. 강화 총알 havingEnhancedBullet

  const [havingBlockScreen, setHavingBlockScreen] = useState(0);
  const [havingBlockGPS, setHavingBlockGPS] = useState(0);
  const [havingEnhancedBullet, setHavingEnhancedBullet] = useState(0);

  useEffect(() => {
    const blockScreenItem = itemList.find(item => item.itemId === 1); // 스텔스 망토의 itemId
    const blockGPSItem = itemList.find(item => item.itemId === 2); // 방해 폭탄의 itemId
    const enhancedBulletItem = itemList.find(item => item.itemId === 3); // 강화 총알의 itemId

    setHavingBlockScreen(blockScreenItem ? blockScreenItem.count : 0);
    setHavingBlockGPS(blockGPSItem ? blockGPSItem.count : 0);
    setHavingEnhancedBullet(enhancedBulletItem ? enhancedBulletItem.count : 0);
  }, [itemList]);

  return {
    havingBlockScreen,
    setHavingBlockScreen,
    havingBlockGPS,
    setHavingBlockGPS,
    havingEnhancedBullet,
    setHavingEnhancedBullet,
  };
};

export default useItemHave;
