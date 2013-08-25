package ee.tmtu.ld27;

import ee.tmtu.libludum.assets.AssetManager;

import java.io.*;
import java.util.ArrayList;

public class HiScoreList {

    public ArrayList<HiScore> round1;
    public ArrayList<HiScore> round2;
    public ArrayList<HiScore> round3;
    public ArrayList<HiScore> round4;
    public ArrayList<HiScore> round5;

    public HiScoreList() {
        this.round1 = new ArrayList<>();
        this.round2 = new ArrayList<>();
        this.round3 = new ArrayList<>();
        this.round4 = new ArrayList<>();
        this.round5 = new ArrayList<>();
    }

    public void save(String res) {
        try {
            FileWriter fw = new FileWriter(res);
            fw.write(AssetManager.gson.toJson(this));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HiScoreList from(String res) {
        try {
            return AssetManager.gson.fromJson(new FileReader(res), HiScoreList.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HiScoreList();
    }

}
