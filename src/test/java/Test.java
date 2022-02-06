import com.zyy.community.dto.HotTagDTO;

import java.util.PriorityQueue;

public class Test {

    @org.junit.Test
    public void test() {
        System.out.println(System.currentTimeMillis());
    }

    @org.junit.Test
    public void test2() {
        PriorityQueue<HotTagDTO> priorityQueue = new PriorityQueue<>(3);
        priorityQueue.add(new HotTagDTO("zhangsan1", 10));
        priorityQueue.add(new HotTagDTO("zhangsan2", 15));
        priorityQueue.add(new HotTagDTO("zhangsan3", 1));
        priorityQueue.poll();
        priorityQueue.add(new HotTagDTO("zhangsan4", 6));
        System.out.println(priorityQueue);
    }

    @org.junit.Test
    public void test3() {
        System.out.println("\\?");
    }

}