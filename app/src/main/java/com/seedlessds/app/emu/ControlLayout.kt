package com.seedlessds.app.emu

import android.content.Context

object ControlLayout {
    private const val PREFS = "ctrl_layout"
    val ids = listOf("dpad", "face", "lbtn", "rbtn", "startselect", "menu", "extra")

    private val offsets = HashMap<String, Pair<Float, Float>>()
    private val scales = HashMap<String, Float>()
    private val hidden = HashSet<String>()

    @Volatile var topScale: Float = 1f
    @Volatile var topScaleX: Float = 1f
    @Volatile var topScaleY: Float = 1f
    @Volatile var topOffX: Float = 0f
    @Volatile var topOffY: Float = 0f
    @Volatile var botScale: Float = 1f
    @Volatile var botScaleX: Float = 1f
    @Volatile var botScaleY: Float = 1f
    @Volatile var botOffX: Float = 0f
    @Volatile var botOffY: Float = 0f
    @Volatile var extScale: Float = 1f
    @Volatile var backgroundUri: String = ""

    private var currentLandscape: Boolean = false

    fun isHidden(id: String): Boolean = id in hidden
    fun setHidden(id: String, h: Boolean) { if (h) hidden.add(id) else hidden.remove(id) }

    private fun prefix(landscape: Boolean) = if (landscape) "ls_" else "pt_"

    fun load(ctx: Context, landscape: Boolean = false) {
        currentLandscape = landscape
        val p = ctx.getSharedPreferences(PREFS, 0)
        val pref = prefix(landscape)
        offsets.clear(); scales.clear()
        for (id in ids) {
            offsets[id] = p.getFloat("${pref}${id}_x", 0f) to p.getFloat("${pref}${id}_y", 0f)
            scales[id] = p.getFloat("${pref}${id}_s", 1f)
        }
        val oldScale = p.getFloat("${pref}screen_scale", p.getFloat("screen_scale", 1f))
        val oldOffX = p.getFloat("${pref}screen_offx", p.getFloat("screen_offx", 0f))
        val oldOffY = p.getFloat("${pref}screen_offy", p.getFloat("screen_offy", 0f))
        topScale = p.getFloat("${pref}top_scale", p.getFloat("top_scale", oldScale))
        topScaleX = p.getFloat("${pref}top_scale_x", topScale)
        topScaleY = p.getFloat("${pref}top_scale_y", topScale)
        topOffX = p.getFloat("${pref}top_offx", p.getFloat("top_offx", oldOffX))
        topOffY = p.getFloat("${pref}top_offy", p.getFloat("top_offy", oldOffY))
        botScale = p.getFloat("${pref}bot_scale", p.getFloat("bot_scale", oldScale))
        botScaleX = p.getFloat("${pref}bot_scale_x", botScale)
        botScaleY = p.getFloat("${pref}bot_scale_y", botScale)
        botOffX = p.getFloat("${pref}bot_offx", p.getFloat("bot_offx", oldOffX))
        botOffY = p.getFloat("${pref}bot_offy", p.getFloat("bot_offy", oldOffY))
        extScale = p.getFloat("${pref}ext_scale", p.getFloat("ext_scale", 1f))
        hidden.clear()
        p.getStringSet("${pref}hidden", null)?.let { hidden.addAll(it) }
        backgroundUri = p.getString("${pref}background_uri", p.getString("background_uri", "")) ?: ""
    }

    fun offsetOf(id: String): Pair<Float, Float> = offsets[id] ?: (0f to 0f)
    fun scaleOf(id: String): Float = scales[id] ?: 1f

    fun save(
        ctx: Context,
        offsetValues: Map<String, Pair<Float, Float>>,
        scaleValues: Map<String, Float>,
        landscape: Boolean = currentLandscape,
    ) {
        currentLandscape = landscape
        val e = ctx.getSharedPreferences(PREFS, 0).edit()
        val pref = prefix(landscape)
        for ((id, off) in offsetValues) { e.putFloat("${pref}${id}_x", off.first); e.putFloat("${pref}${id}_y", off.second) }
        for ((id, s) in scaleValues) { e.putFloat("${pref}${id}_s", s) }
        e.putFloat("${pref}top_scale", topScale)
        e.putFloat("${pref}top_scale_x", topScaleX)
        e.putFloat("${pref}top_scale_y", topScaleY)
        e.putFloat("${pref}top_offx", topOffX)
        e.putFloat("${pref}top_offy", topOffY)
        e.putFloat("${pref}bot_scale", botScale)
        e.putFloat("${pref}bot_scale_x", botScaleX)
        e.putFloat("${pref}bot_scale_y", botScaleY)
        e.putFloat("${pref}bot_offx", botOffX)
        e.putFloat("${pref}bot_offy", botOffY)
        e.putFloat("${pref}ext_scale", extScale)
        e.putStringSet("${pref}hidden", HashSet(hidden))
        e.putFloat("${pref}screen_scale", topScale)
        e.putFloat("${pref}screen_offx", topOffX)
        e.putFloat("${pref}screen_offy", topOffY)
        e.apply()
        offsets.putAll(offsetValues); scales.putAll(scaleValues)
    }

    fun setBackground(ctx: Context, uri: String, landscape: Boolean = currentLandscape) {
        val pref = prefix(landscape)
        ctx.getSharedPreferences(PREFS, 0).edit().putString("${pref}background_uri", uri).apply()
        backgroundUri = uri
    }

    fun reset(ctx: Context, landscape: Boolean = currentLandscape) {
        val pref = prefix(landscape)
        val e = ctx.getSharedPreferences(PREFS, 0).edit()
        for (id in ids) {
            e.remove("${pref}${id}_x")
            e.remove("${pref}${id}_y")
            e.remove("${pref}${id}_s")
        }
        e.remove("${pref}top_scale")
        e.remove("${pref}top_scale_x")
        e.remove("${pref}top_scale_y")
        e.remove("${pref}top_offx")
        e.remove("${pref}top_offy")
        e.remove("${pref}bot_scale")
        e.remove("${pref}bot_scale_x")
        e.remove("${pref}bot_scale_y")
        e.remove("${pref}bot_offx")
        e.remove("${pref}bot_offy")
        e.remove("${pref}ext_scale")
        e.remove("${pref}hidden")
        e.remove("${pref}screen_scale")
        e.remove("${pref}screen_offx")
        e.remove("${pref}screen_offy")
        e.remove("${pref}background_uri")
        e.apply()
        topScale = 1f; topScaleX = 1f; topScaleY = 1f; topOffX = 0f; topOffY = 0f
        botScale = 1f; botScaleX = 1f; botScaleY = 1f; botOffX = 0f; botOffY = 0f
        extScale = 1f
        load(ctx, landscape)
    }
}
