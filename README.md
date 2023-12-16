# Swipeable Card
Android Library for Tinder-like swipeable cards

## How to install via GRADLE

- Add the following code in your system.gradle file:
```
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

- Add the following dependencies:
```
dependencies {
        implementation 'com.github.shiva-kumar-R:swipeablecard:Tag'
}
```

## How to use

- Add swipeableCard() to the modifier to enable swipes. rememberSwipeableCardStateis there to manage state of the modifier for swipe.
- By default swipe is horizontal only, if swipe needs to be available in blocked in other directions then pass those directions in blockedDirections.
- onSwiped is callback when swipe is completed.
- onSwipeCacnel is callback when swipe is cancelled.

```
val state = rememberSwipeableCardState()

Box(
    modifier = Modifier
        .swipableCard(
            blockedDirections = listOf(Direction.Down),
            state = state,
            onSwiped = { direction ->
                println("The card was swiped in $direction")
            },
            onSwipeCancel = {
                println("The swiping was cancelled")
            }
        )
) {
}
```
