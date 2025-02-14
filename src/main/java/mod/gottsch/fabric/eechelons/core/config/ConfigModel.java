package mod.gottsch.fabric.eechelons.core.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

/**
 * @author by Mark Gottschling on 3/7/2023
 */
@Modmenu(modId = "eechelons")
@Config(name = "eechelons", wrapperName = "MyConfig")
public class ConfigModel {

    @SectionHeader("clientProperties")
    public boolean showClientHud = true;
    public int hudXOffset = 0;
    public int hudYOffset = 0;
    public boolean useDarkHud = true;
    public boolean enableWailaIntegration = true;

    @SectionHeader("serverProperties")

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean showHud = true;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    public boolean hudRangeEnabled = false;

    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @RangeConstraint(min = 1, max = 100)
    public int hudRange = 3;
}

