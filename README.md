# potato-battery

The Potato Battery mod is intended to provide a fun and more interesting take on power in Minecraft without overcomplicating it to the point where you need a physics degree in order to understand it. It’s built with the Architectury api, and written mostly in Kotlin. This allows it to be loader independent. The system is intended to be easily expanded upon and integrated with, while also allowing for straightforward conversion to a more typical system such as FE or RF.

## Requirements for new additions:
*In order to keep component additions simple, ask first these principles five:*

Is it simple? |
John Modplayer must understand how the component should work by name

Is it intuitive? |
Is the design of the component something John Modplayer can quickly learn to understand?

Is it fun? |
Will John Modplayer have fun circumventing the challenges proposed by the system, or will he be frustrated at its difficulty?

Is it necessary? |
Does the mod benefit from having the new component integrated into the existing system?

## Voltage and Amperage
Power in Potato Battery has two main concepts; simplified versions of real world equivalents. Voltage is the measure of electric potential, or essentially the ‘power’, and Amperage is the measure of current, or the actual amount of electricity. That’s not exactly how it works, but as stated this isn’t for people with a physics degree.

Certain machines or apparatus will have a maximum voltage that they can take. A higher voltage means that they’ll work faster or more efficiently, but also comes with the risk of overloading said machine. Depending on the amount of voltage supplied it will use more or less amperage.

## Components
Amplifier

Amplifies voltage depending on setting configured in amplifier 

Diode

Only allows current in one direction. Has a dropoff of 0.3V

Constant Current Diode

LED

Light emitting counterpart of a diode, colorable

Relay

A simple switch with 2 inputs and 1 output

Resistor

Has a certain amount of resistance (in Ohms)

Transformer

Transforms AC to DC at a ratio configured in the block

Meter

Speaker

Emits sounds based on the voltage inputted.

Cable

Connects components

Fuse

Pops when a certain amount of amperage flows through.

Capacitor

Stores temporary energy and tries to reach a equilibrium with the electrical system

Battery

Stores certain amount of power, outputs it at a fixed voltage range

Converter

Panel that converts DC power into other mods’ power (RF, FE, Tech Reborn energy)

Has 2 cable socket inputs for Potato Battery energy on the top, and a display that shows how much power is being output

Has 1 output on the bottom to output into the input of other mods’ machines


## Capacitors and Batteries
Capacitors in PB are designed around more of what they do in the real world than what they do in most MC mods- they’re a quick charger, and a quick releaser of energy- and don’t last very long or hold charge very well over time. They’re useful for a buffer in a system to prevent power fluctuating (if your generation sucks ass) or for dense cable hells where you need to hold off and send power down specific lines quickly.

Batteries are the opposite- they charge slow, and release slow- but they don’t lose energy over time, and have a much greater capacity. Batteries have a maximum voltage however, where capacitors don’t- so be careful not to overdo your charging.

## Resistors
In PB, Resistors are your grid’s bread and butter for when you need to not overload every system on the network at once like a clown. They’re horrifically oversimplified compared to their real life counterparts, but in essence, transistors raise the voltage, and resistors lower it. However- power doesn’t just go away or magically appear- transistors will need two inputs to combine their voltages, and resistors will need somewhere to send that extra energy.


