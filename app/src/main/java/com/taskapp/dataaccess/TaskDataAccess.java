package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import com.taskapp.model.Task;
import com.taskapp.model.User;
import java.io.IOException;
import java.io.Writer;

public class TaskDataAccess {

    private final String filePath;

    private final UserDataAccess userDataAccess;

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv";
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * @param filePath
     * @param userDataAccess
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath;
        this.userDataAccess = userDataAccess;
    }

    /**
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if(values.length != 4) continue;

                int code = Integer.parseInt(values[0]);
                String name = values[1];
                int status = Integer.parseInt(values[2]);
                int repUser = Integer.parseInt(values[3]);
                User repUser2 = userDataAccess.findByCode(repUser);

                Task task = new Task(code, name, status, repUser2);
                tasks.add(task);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    public void save(Task task) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath,true))) {
            String line = createLine(task);
            bw.newLine();
            bw.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを1件取得します。
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
    public Task findByCode(int code) {
            Task task = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while((line = reader.readLine()) != null) {
                String[] values =line.split(",");

                int code1 = Integer.parseInt(values[0]);
                if (code != code1) continue;
                
                String name = values[1];
                int status = Integer.parseInt(values[2]);
                int repUser = Integer.parseInt(values[3]);
                User repUser2 = userDataAccess.findByCode(repUser);
                
                task = new Task(code1, name, status, repUser2);
                break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return task;
    }

    /**
     * タスクデータを更新します。
     * @param updateTask 更新するタスク
     */
    public void update(Task updateTask) {
        List<Task> tasks = findAll();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Code,Name,Status,Rep_User_Code");
            String line;
            for (Task task : tasks) {
                if(task.getCode() == updateTask.getCode()) {
                    line = createLine(updateTask);
                } else {
                    line = createLine(task);
                }
                bw.newLine();
                bw.write(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを削除します。
     * @param code 削除するタスクのコード
     */
    // public void delete(int code) {
    //     List<Task> tasks = findAll();
    //     try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
    //         bw.write("Code,Name,Status,Rep_User_Code");

    //         for(Task task : tasks) {
    //             if(task.getCode() != code) {
    //                 String line = createLine(task);
    //                 bw.write(line);
    //                 bw.newLine();
    //             }
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    private String createLine(Task task) {
        return task.getCode() + "," + task.getName() + "," +task.getStatus() + "," + task.getRepUser().getCode();
    }
}