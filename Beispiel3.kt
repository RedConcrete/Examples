package team12.exercise2.code.screens

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import ktx.ashley.entity
import ktx.ashley.with
import ktx.async.KtxAsync
import ktx.scene2d.*
import team12.exercise2.code.ecs.assets.MusicAsset
import team12.exercise2.code.ecs.backend.*
import team12.exercise2.code.ecs.backend.events.GameScreenEvent
import team12.exercise2.code.ecs.component.*
import team12.exercise2.code.main.DarkAndSilent
import team12.exercise2.code.ui.LabelStyles
import team12.exercise2.code.ui.SkinTextButton
import team12.exercise2.code.ui.SkinTouchpad
import kotlin.math.min

private const val MAX_DELTA_TIME: Float = 1 / 20f

private lateinit var dash: TextButton
private lateinit var back: TextButton
private lateinit var catch: TextButton
private lateinit var yes: TextButton
private lateinit var no: TextButton

private lateinit var textLabel: Label
private lateinit var textLabelExit: Label
private lateinit var timeLabel: Label

private lateinit var playersInput_touchpad: Touchpad

private var joystickLeft: Boolean = false


class GameScreen(game: DarkAndSilent, private val engine: Engine = game.engine, mapKey:String=Constants.MAP1_KEY) : Screens(game) {

    lateinit var player: Entity
    var events = GameScreenEvent(this, game)
    private var renderPreference = events.getRenderPreference()
    private var dashPress = false
    private var backPress = false
    private var caught = false
    private var yesPress = false
    private var noPress = false
    private var catchButtonPressed = false
    private val gameMapKey = mapKey

    init {
        if (!game.playerIsHunter) {
            events = GameScreenEvent(this, game, Constants.LOWER_LEFT_X, Constants.LOWER_LEFT_Y)
        }
    }

    /**
     *
     */
    override fun show() {

        events.setInitialSpawn()
        audioService.stop()
        audioService.play(MusicAsset.GAME_1)

        KtxAsync.launch {
            assetLaunch().joinAll()
        }

        setupUI()
        createEntities()

        wallRenderer.projectionMatrix = OrthographicCamera(gameViewport.worldWidth, gameViewport.worldHeight).combined
        wallRenderer.color = Color.BLACK

    }

    /**
     * creates all entiteies
     */
    private fun createEntities() {
        player = engine.entity {
            with<TransformComponent>()
        }
    }

    /**
     * hides the stage if called
     */
    override fun hide() {
        stage.clear()
    }

    /**
     * this methode creates the stage UI
     */
    private fun setupUI() {
        stage.actors {
            table {
                timeLabel = label("0:00", LabelStyles.TITLE_FONT.name) {
                    setFontScale(2f)
                }
                getCell(timeLabel).padTop(200f)
                getCell(timeLabel).padLeft(500f)
                getCell(timeLabel).minWidth(200f)

                back = textButton("Exit", SkinTextButton.BUTTON_GRADIENT.name) {
                }
                getCell(back).padTop(200f)
                getCell(back).padRight(600f)
                getCell(back).align(Align.right)
                row()

                /**
                 * This if() generates the full righthandmode UI
                 */
                if (!joystickLeft) {

                    textLabel = label("", LabelStyles.TITLE_FONT.name) {
                        isVisible = false
                        setAlignment(Align.center)
                        setFontScale(2f)
                    }

                    getCell(textLabel).colspan(2)
                    row()
                    textLabelExit = label("", LabelStyles.TITLE_FONT.name) {
                        isVisible = false
                        setAlignment(Align.center)
                        setFontScale(2f)
                    }
                    getCell(textLabelExit).colspan(2)
                    row()

                    yes = textButton("Yes", SkinTextButton.BUTTON_GRADIENT.name) {
                        isVisible = false
                    }
                    getCell(yes).colspan(2)
                    getCell(yes).minWidth(200f)
                    row()

                    no = textButton("No", SkinTextButton.BUTTON_GRADIENT.name) {
                        isVisible = false
                    }
                    getCell(no).colspan(2)
                    getCell(no).minWidth(200f)
                    row()

                    dash = textButton("DASH", SkinTextButton.BUTTON_GRADIENT.name) {
                    }
                    getCell(dash).padLeft(400f)

                    if (game.playerIsHunter) {
                        catch = textButton("CATCH", SkinTextButton.BUTTON_GRADIENT.name) {
                        }
                        getCell(catch).padRight(1800f)
                        getCell(catch).align(Align.left)
                        row()

                    } else {
                        val space = label("", LabelStyles.TITLE_FONT.name) {
                            isVisible = false
                            setAlignment(Align.center)
                            setFontScale(4f)
                        }
                        getCell(space).padRight(1800f)
                        row()
                    }

                    playersInput_touchpad = touchpad(1f, SkinTouchpad.DEFAULT.name) {
                    }

                    getCell(playersInput_touchpad).minHeight(500f)
                    getCell(playersInput_touchpad).minWidth(500f)
                    getCell(playersInput_touchpad).padLeft(1300f)
                    getCell(playersInput_touchpad).padBottom(150f)
                    getCell(playersInput_touchpad).colspan(2)

                }

                /**
                 * This if() generates the full lefthandmode UI
                 */
                if (joystickLeft) {
                    textLabel = label("", LabelStyles.TITLE_FONT.name) {
                        isVisible = false
                        setAlignment(Align.center)
                        setFontScale(2f)
                    }
                    getCell(textLabel).colspan(2)
                    row()
                    textLabelExit = label("", LabelStyles.TITLE_FONT.name) {
                        isVisible = false
                        setAlignment(Align.center)
                        setFontScale(2f)
                    }
                    getCell(textLabelExit).colspan(2)
                    row()

                    yes = textButton("Yes", SkinTextButton.BUTTON_GRADIENT.name) {
                        isVisible = false
                    }
                    getCell(yes).colspan(2)
                    getCell(yes).minWidth(200f)
                    row()

                    no = textButton("No", SkinTextButton.BUTTON_GRADIENT.name) {
                        isVisible = false
                    }
                    getCell(no).colspan(2)
                    getCell(no).minWidth(200f)
                    row()

                    dash = textButton("DASH", SkinTextButton.BUTTON_GRADIENT.name) {

                    }
                    getCell(dash).align(Align.right)
                    getCell(dash).padLeft(1800f)

                    if (game.playerIsHunter) {
                        catch = textButton("CATCH", SkinTextButton.BUTTON_GRADIENT.name) {
                        }
                        getCell(catch).padRight(400f)
                        row()

                    } else {
                        label("", LabelStyles.TITLE_FONT.name) {
                            isVisible = false
                            setFontScale(4f)
                        }
                        row()
                    }

                    playersInput_touchpad = touchpad(1f, SkinTouchpad.DEFAULT.name) {
                    }

                    getCell(playersInput_touchpad).minHeight(500f)
                    getCell(playersInput_touchpad).minWidth(500f)
                    getCell(playersInput_touchpad).padRight(1300f)
                    getCell(playersInput_touchpad).padBottom(150f)
                    getCell(playersInput_touchpad).colspan(2)

                }

                setFillParent(true)
                pack()
            }

        }
        stage.isDebugAll = false
    }

    val sampleWalls = SegmentList(game.resourceManager, mapKey)
    val wallRenderer = ShapeRenderer()
    var stepAngle =
        0.3f// Anzahl der Rays (oder Genauigkeitsgrad der Welle) (kleiner = genauer); je genauer umso rechenaufwendiger // val
    var stepDistance = 10f
    val rayslist = ArrayList<ExpandingObject>()
    val initialColor: Color = Color.RED
    val finalColor: Color = Color.YELLOW
    val positionVector = Vector2()

    /**
     *Holds all event calls necessary to execute the logic during gameplay:
     * rendering, collisions, button events, server requests and responses, timer checks.
     */
    override fun render(delta: Float) {
        (game.batch as SpriteBatch).renderCalls = 0
        engine.update(min(MAX_DELTA_TIME, delta))


        events.drawWalls()
        events.drawExpandingObject(renderPreference)
        events.checkRaysValidity() // very important call, without it infinitely many objects will be rendered

        positionVector.x = Gdx.input.x.toFloat()
        positionVector.y = Gdx.input.y.toFloat()
        gameViewport.unproject(positionVector)

        isDashPressedCheck()
        isntCaughtCheck()
        events.generateReceivedBurst()

        events.checkTouchpadInput(playersInput_touchpad) // just comment this line to disable movement
        events.checkSpeed()

        gameTimeUpdate()
        hasCooldown()
        isGameFinisedCheck()
        isHunterCheck()
        catchSuccessfulCheck()
        isCaughtCheck()
        backIsPressedCheck()
        yesIsPressedCheck()
        noIsPressedCheck()
        isEliminatedFromLobbyCheck()
        events.sendPosition()
        events.sendPing()

        //events.drawWaves()
        //events.drawRays()
        getTimer()

        engine.update(delta)
        audioService.update()
        stage.run {
            viewport.apply()
            act()
            draw()
        }

        events.drawPlayerDot()
    }

    /**
     *
     */
    private fun isntCaughtCheck() {
        if (!caught) {
            events.generateTimedBurst()

        }
    }

    /**
     *
     */
    private fun gameTimeUpdate() {
        val updatedTime = events.getUpdatedGameTimeSecs()
        if (updatedTime != null) {
            timeLabel.setText(events.convertSecsToTimeDisplayString(updatedTime))
        }
    }

    /**
     *
     */
    private fun hasCooldown() {
        if (events.getDashCooldown() == 0) {
            dash.setText(" Dash \nReady")
        } else {
            dash.setText(" Dash \n ${events.getDashCooldown()}")
        }
        if (game.playerIsHunter) {
            catch.isVisible = true
            if (events.getCatchCooldown() == 0) {
                catch.setText(" CATCH \nReady")
            } else {
                catch.setText(" CATCH \n ${events.getCatchCooldown()}")
            }
        }
    }

    /**
     *
     */
    private fun isGameFinisedCheck() {
        if (events.gameFinished()) {
            audioService.stop()
            audioService.play(MusicAsset.GAME_0)
            game.addScreen(LobbyScreen(game, game.currentLobby))
            game.setScreen<LobbyScreen>()
            game.removeScreen<GameScreen>()
            engine.removeEntity(player)
            dispose()
        }
    }

    /**
     *
     */
    private fun getTimer() {
        if (events.getTextMassageTimer() == 0) {
            textLabel.isVisible = false
        }
    }

    /**
     *
     */
    private fun catchSuccessfulCheck() {
        if (events.catchSuccessful()) {
            events.startMessageTimer()
            textLabel.setText("You caught someone")
            textLabel.isVisible = true
        }
    }

    /**
     *
     */
    private fun isEliminatedFromLobbyCheck() {
        if (events.eliminatedFromLobby()) {
            audioService.stop()
            audioService.play(MusicAsset.GAME_0)
            game.addScreen(MenuScreen(game))
            game.setScreen<MenuScreen>()
            game.removeScreen<GameScreen>()
            engine.removeEntity(player)
            dispose()
        }
    }

    /**
     *
     */
    fun getJoystickLeft(): Boolean {
        return joystickLeft
    }

    /**
     *
     */
    fun setJoystickLeft(joyStickLeft: Boolean) {
        joystickLeft = joyStickLeft
    }

    /**
     *
     */
    private fun noIsPressedCheck() {
        if (no.isPressed) {
            if (!noPress) {
                noPress = true
                textLabelExit.isVisible = false
                yes.isVisible = false
                no.isVisible = false
            }
        } else noPress = false
    }

    /**
     *
     */
    private fun yesIsPressedCheck() {
        if (yes.isPressed) {
            if (!yesPress) {
                yesPress = true
                events.resetToMenu()
                audioService.stop()
                audioService.play(MusicAsset.GAME_0)
                game.addScreen(MenuScreen(game))
                game.setScreen<MenuScreen>()
                game.removeScreen<GameScreen>()
                engine.removeEntity(player)
                dispose()
            }
        } else yesPress = false
    }

    /**
     *
     */
    private fun isDashPressedCheck() {
        if (dash.isPressed) {
            if (!dashPress) {
                dashPress = true
                if (!events.hasCooldown()) {
                    events.performDash()
                }
            }
        } else dashPress = false
    }

    /**
     *
     */
    private fun backIsPressedCheck() {
        if (back.isPressed) {

            if (!backPress) {
                backPress = true
                textLabelExit.setText("EXIT ?")
                textLabelExit.isVisible = true
                yes.isVisible = true
                no.isVisible = true

            }

        } else backPress = false
    }

    /**
     *
     */
    private fun isCaughtCheck() {
        if (events.isCaught()) {
            events.startMessageTimer()
            engine.removeEntity(player)
            textLabel.setText("You got caught")
            textLabel.isVisible = true
            textLabelExit.isVisible = true
            dash.isVisible = false
            caught = true

        }
    }

    /**
     * checks if the player is a hunter
     */
    private fun isHunterCheck() {
        if (game.playerIsHunter) {
            if (catch.isPressed) {
                if (!catchButtonPressed) {
                    catchButtonPressed = true
                    events.attemptCatch()
                }
            } else catchButtonPressed = false
        }
    }

}
