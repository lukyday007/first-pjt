import { useState } from 'react';
import { Button } from '@components/ui/Button';

export default function Home() {
  const [count, setCount] = useState(0);

  return (
    <div>
      <h1>HOME</h1>
      <Button onClick={() => setCount(count + 1)}>TEST : {count}</Button>
    </div>
  );
}
