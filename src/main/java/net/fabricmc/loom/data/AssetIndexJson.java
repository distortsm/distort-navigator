/*
 * This file is part of fabric-loom, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016, 2017, 2018 FabricMC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.fabricmc.loom.data;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.taskdefs.Length;

import net.fabricmc.loom.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by covers1624 on 5/02/19.
 */
public class AssetIndexJson {

    private static final Gson gson = new Gson();

    public Map<String, AssetObject> objects;
    public boolean virtual;

    public static AssetIndexJson fromJson(File file) {
        return Utils.fromJson(gson, file, AssetIndexJson.class);
    }

    public static AssetIndexJson fromStarMadeChecksums(File file) throws IOException {
        AssetIndexJson assetIndex = new AssetIndexJson();
        assetIndex.objects = new HashMap<String,AssetObject>();

        FileUtils.readLines(file, "UTF-8").stream().forEach(entry -> {
            if (Strings.isNullOrEmpty(entry)) {
                return;
            }
            try {
                // Although the checksums are space seperated. it doesn't account for asset names with spaces...
                String[] tokens = entry.split(" ");
                String name = entry.substring(0, entry.length() - tokens[tokens.length - 1].length() - tokens[tokens.length - 2].length() - 2);
                AssetObject asset = assetIndex.new AssetObject() {{
                        size = Integer.parseInt(tokens[tokens.length - 2]);
                        hash = tokens[tokens.length - 1];
                    }};
                assetIndex.objects.put(name, asset);
            }
            catch(Exception e) { throw new RuntimeException(e); }
        });

        return assetIndex;
    }

    public class AssetObject {

        public String hash;
        public int size;
    }
}
