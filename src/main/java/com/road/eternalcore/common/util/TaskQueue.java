package com.road.eternalcore.common.util;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Consumer;

public class TaskQueue {
    // 给一些需要反复迭代的方法使用，避免递归
    private Queue<Consumer<TaskQueue>> tasks = new ArrayDeque<>();
    public TaskQueue(){}
    public void addTask(Consumer<TaskQueue> newTask){
        tasks.offer(newTask);
    }
    public void run(){
        while (!tasks.isEmpty()){
            Consumer<TaskQueue> task = tasks.poll();
            task.accept(this);
        }
    }
    public static void runTask(Consumer<TaskQueue> task){
        TaskQueue taskQueue = new TaskQueue();
        taskQueue.addTask(task);
        taskQueue.run();
    }
}
