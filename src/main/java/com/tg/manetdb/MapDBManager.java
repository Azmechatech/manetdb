/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tg.manetdb;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

/**
 *
 * @author manoj.kumar
 */
public class MapDBManager {

    public static float version = 1.0f;
    public DB mapdb = null;
    HTreeMap<Long, String> uploadChunk;
    HTreeMap<Long, byte[]> paramCache;

    public File HTTPCacheDBFolder = null;
    public Map<String, String> mapDBMeta;

    /**
     *
     * @param HTTPCacheDBFolder
     * @param readOnly
     */
    public MapDBManager(File HTTPCacheDBFolder, boolean readOnly) {
        if (HTTPCacheDBFolder.isDirectory()) {
            File dbVersionFolder = new File(HTTPCacheDBFolder.getAbsolutePath() + "/" + version);
            this.HTTPCacheDBFolder = dbVersionFolder;
            File dbFile = new File(dbVersionFolder.getAbsolutePath() + "/MapDBManet.mapdb");
            if (readOnly) {
                init(dbFile, true);
            } else {

                if (dbVersionFolder.exists()) {
                    init(dbFile, false);
                    this.mapDBMeta = mapdb.hashMap("mapDBMeta", Serializer.STRING, Serializer.STRING).createOrOpen();
                } else {//First time creation
                    System.out.println("Creating MapDBManet.mapdb at >> " + dbVersionFolder.getAbsolutePath());
                    dbVersionFolder.mkdirs();
                    init(dbFile, false);
                    this.mapDBMeta = mapdb.hashMap("mapDBMeta", Serializer.STRING, Serializer.STRING).createOrOpen();
                    this.mapDBMeta.put("version", version + "");
                    this.mapDBMeta.put("CreatedOn", new Date().toString());
                }
            }
        }
    }

    /**
     *
     * @param dbFile
     * @param readOnly
     */
    private void init(File dbFile, boolean readOnly) {

        if (readOnly) {
            this.mapdb = DBMaker.fileDB(dbFile).readOnly().fileChannelEnable().fileMmapEnable().fileMmapEnableIfSupported().fileMmapPreclearDisable().transactionEnable().cleanerHackEnable().make();
        } else {
            this.mapdb = DBMaker.fileDB(dbFile).fileChannelEnable().fileMmapEnable().fileMmapEnableIfSupported().fileMmapPreclearDisable().transactionEnable().cleanerHackEnable().make();
        }
        this.uploadChunk = mapdb.hashMap("uploadChunk", Serializer.LONG, Serializer.STRING).expireAfterCreate(1, TimeUnit.HOURS).expireAfterGet().createOrOpen();
        this.paramCache = mapdb.hashMap("paramCache", Serializer.LONG, Serializer.BYTE_ARRAY_NOSIZE).expireAfterCreate(1, TimeUnit.HOURS).expireAfterGet().createOrOpen();

    }
    
    public boolean commit() {
        mapdb.commit();
        return true;
    }

}
