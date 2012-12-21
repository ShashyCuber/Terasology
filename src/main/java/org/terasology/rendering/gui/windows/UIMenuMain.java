/*
 * Copyright 2012 Benjamin Glatzel <benjamin.glatzel@me.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.gui.windows;

import javax.vecmath.Vector2f;

import org.terasology.asset.Assets;
import org.terasology.config.ModConfig;
import org.terasology.game.CoreRegistry;
import org.terasology.game.GameEngine;
import org.terasology.game.modes.StateLoading;
import org.terasology.game.types.GameType;
import org.terasology.game.types.SurvivalType;
import org.terasology.logic.manager.Config;
import org.terasology.network.NetworkMode;
import org.terasology.rendering.gui.framework.UIDisplayElement;
import org.terasology.rendering.gui.framework.events.ClickListener;
import org.terasology.rendering.gui.widgets.UIButton;
import org.terasology.rendering.gui.widgets.UIImage;
import org.terasology.rendering.gui.widgets.UILabel;
import org.terasology.rendering.gui.widgets.UIWindow;
import org.terasology.world.WorldInfo;
import org.terasology.world.generator.core.FloraGenerator;
import org.terasology.world.generator.core.ForestGenerator;
import org.terasology.world.generator.core.PerlinTerrainGenerator;
import org.terasology.world.liquid.LiquidsGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Main menu screen.
 *
 * @author Anton Kireev <adeon.k87@gmail.com>
 */
public class UIMenuMain extends UIWindow {

    private final UIImage title;

    private final UIButton exitButton;
    private final UIButton singlePlayerButton;
    private final UIButton configButton;
    private final UIButton joinButton;

    final UILabel version;

    public UIMenuMain() {
        setId("main");
        setBackgroundImage("engine:menubackground");
        setModal(true);
        maximize();
        
        title = new UIImage(Assets.getTexture("engine:terasology"));
        title.setSize(new Vector2f(512f, 128f));
        title.setHorizontalAlign(EHorizontalAlign.CENTER);
        title.setPosition(new Vector2f(0f, 128f));
        title.setVisible(true);

        version = new UILabel("Pre Alpha");
        version.setHorizontalAlign(EHorizontalAlign.CENTER);
        version.setPosition(new Vector2f(0f, 230f));
        version.setVisible(true);
        version.setTextShadow(true);

        exitButton = new UIButton(new Vector2f(256f, 32f), UIButton.ButtonType.NORMAL);
        exitButton.getLabel().setText("Exit Terasology");
        exitButton.addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                CoreRegistry.get(GameEngine.class).shutdown();
            }
        });
        exitButton.setHorizontalAlign(EHorizontalAlign.CENTER);
        exitButton.setPosition(new Vector2f(0f, 300f + 5 * 40f));
        exitButton.setVisible(true);
        
        configButton = new UIButton(new Vector2f(256f, 32f), UIButton.ButtonType.NORMAL);
        configButton.getLabel().setText("Settings");
        configButton.addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                getGUIManager().openWindow("config");
            }
        });
        configButton.setHorizontalAlign(EHorizontalAlign.CENTER);
        configButton.setPosition(new Vector2f(0f, 300f + 3 * 40f));
        configButton.setVisible(true);

        singlePlayerButton = new UIButton(new Vector2f(256f, 32f), UIButton.ButtonType.NORMAL);
        singlePlayerButton.getLabel().setText("Single player");
        singlePlayerButton.addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {
                getGUIManager().openWindow("singleplayer");
            }
        });

        singlePlayerButton.setHorizontalAlign(EHorizontalAlign.CENTER);
        singlePlayerButton.setPosition(new Vector2f(0f, 300f + 40f));
        singlePlayerButton.setVisible(true);

        joinButton = new UIButton(new Vector2f(256f, 32f), UIButton.ButtonType.NORMAL);
        joinButton.getLabel().setText("Join Game");
        joinButton.setHorizontalAlign(EHorizontalAlign.CENTER);
        joinButton.setPosition(new Vector2f(0f, 300f + 2 * 40f));
        joinButton.setVisible(true);
        joinButton.addClickListener(new ClickListener() {
            @Override
            public void click(UIDisplayElement element, int button) {

                ModConfig modConfig = new ModConfig();
                modConfig.copy(CoreRegistry.get(org.terasology.config.Config.class).getDefaultModConfig());

                String[] chunkList = new String[] {
                PerlinTerrainGenerator.class.getName(),
                FloraGenerator.class.getName(),
                LiquidsGenerator.class.getName(),
                ForestGenerator.class.getName()};

                WorldInfo info = new WorldInfo("JoinWorld", org.terasology.logic.manager.Config.getInstance().getDefaultSeed(), org.terasology.logic.manager.Config.getInstance().getDayNightLengthInMs() / 4, chunkList, SurvivalType.class.toString(), modConfig);

                CoreRegistry.put(GameType.class, new SurvivalType());

                Config.getInstance().setDefaultSeed(info.getSeed());
                Config.getInstance().setWorldTitle(info.getTitle());
                Config.getInstance().setChunkGenerator(info.getChunkGenerators());
                CoreRegistry.get(GameEngine.class).changeState(new StateLoading(info, NetworkMode.CLIENT));
            }
        });

        addDisplayElement(title);
        addDisplayElement(version);
        addDisplayElement(configButton);
        addDisplayElement(exitButton);
        addDisplayElement(singlePlayerButton);
        addDisplayElement(joinButton);
    }
}
