import { useState, useContext } from "react";
import { GameContext } from "@/context/GameContext";

const ITEM_IDS = {
  BLOCK_SCREEN: 1,
  BLOCK_GPS: 2,
  ENHANCED_BULLET: 3,
};

const useItemCount = () => {
  const { itemList } = useContext(GameContext);

  // 1. 스텔스 망토 blockScreenCount
  // 2. 방해 폭탄 blockGPSCount
  // 3. 강화 총알 enhancedBulletCount

  const [blockScreenCount, setBlockScreenCount] = useState(0);
  const [blockGPSCount, setBlockGPSCount] = useState(0);
  const [enhancedBulletCount, setEnhancedBulletCount] = useState(0);

  // itemList 최신화 시(axios response) 갱신
  useEffect(() => {
    const blockScreenItem = itemList.find(
      item => item.itemId === ITEM_IDS.BLOCK_SCREEN
    ); // 스텔스 망토의 itemId
    const blockGPSItem = itemList.find(
      item => item.itemId === ITEM_IDS.BLOCK_GPS
    ); // 방해 폭탄의 itemId
    const enhancedBulletItem = itemList.find(
      item => item.itemId === ITEM_IDS.ENHANCED_BULLET
    ); // 강화 총알의 itemId

    setBlockScreenCount(blockScreenItem ? blockScreenItem.count : 0);
    setBlockGPSCount(blockGPSItem ? blockGPSItem.count : 0);
    setEnhancedBulletCount(enhancedBulletItem ? enhancedBulletItem.count : 0);
  }, [itemList]);

  // 아이템 획득, 사용 시 숫자 갱신
  const getItem = itemId => {
    switch (itemId) {
      case ITEM_IDS.BLOCK_SCREEN:
        setBlockScreenCount(prev => prev + 1);
        break;
      case ITEM_IDS.BLOCK_GPS:
        setBlockGPSCount(prev => prev + 1);
        break;
      case ITEM_IDS.ENHANCED_BULLET:
        setEnhancedBulletCount(prev => prev + 1);
        break;
      default:
        break;
    }
  };

  const useItem = itemId => {
    switch (itemId) {
      case ITEM_IDS.BLOCK_SCREEN:
        setBlockScreenCount(prev => (prev > 0 ? prev - 1 : 0));
        break;
      case ITEM_IDS.BLOCK_GPS:
        setBlockGPSCount(prev => (prev > 0 ? prev - 1 : 0));
        break;
      case ITEM_IDS.ENHANCED_BULLET:
        setEnhancedBulletCount(prev => (prev > 0 ? prev - 1 : 0));
        break;
      default:
        break;
    }
  };

  return {
    blockScreenCount,
    blockGPSCount,
    enhancedBulletCount,
    getItem,
    useItem,
  };
};

export default useItemCount;
