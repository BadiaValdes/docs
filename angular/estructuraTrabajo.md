# Estructura

- Esta es la nueva estructura que estamos siguiendo para trabajar en el frontend.

## Estructura de las Carpetas
- `Core`
    - `componentes` (componentes reutilizables a usar de forma global)
    - `exceptions` (Save here all the exceptions throw in app)
    - `guards` (Put here all project's guards)
    - `resolvers` (Put here all resolvers)
    - `theme` (In this area we will put all the CSS files that aims to change the site theme)
- `Shared`
    - `animations` (This part of shared folder is dedicated to store all sites animations) -> You can see an animation example below

    ```typeScript
    export const ROTATE_ANIMATION = trigger('indicatorRotate', [
        state('collapsed', style({ transform: 'rotate(0deg)' })),
        state('expanded', style({ transform: 'rotate(180deg)' })),
        transition(
            'expanded <=> collapsed',
            animate('225ms cubic-bezier(0.4,0.0,0.2,1)')
        ),
        ]);
    ```

    - `auto-complete` (in case you have multiple component that use autocomplete, here you can store the component or others data)
    - `class`: in case you are going to create clases to be extended, this is the place to store it
    - `components`: this is the place for components that will be used in all the modules or components. As an example we have the site's layout and data grid component.
    - `constant`: constants to be use in all the project. Put here the text to be use as placeholder in code to avoid magic string.
    - `controls`: In case you have reusable forms controls.
    - `directives`: Put your directives heres. You know what directives are.
    - `enums`: You know
    - `interfaces`
        - Here also goes the form control interface
    - `pipes`
    - `states` -> If your using rxjs
    - `types`
    - `validators` -> All form validators goes here
- `features` -> One folder to program them all
    - `components`-> Each one of the components goes here

> Now we are going to explain the structure of a component.

-  `class-eq`
    - `Class`
        - A class per `DTO` that will be use to send data to backend.
        - `abstract ModalComponent<T>` this one has the responsibility
            - Use this for common behavior between modals. 
            
            ```ts
              public readonly formPresenter: ModalPresenter = inject(ModalPresenter); // use the inject method to make DI inside an abstract method
              // Also create all the methods that will be implemented in other components
            ```
    - `Interfaces` -> Like always
    - `Modal`
        - `ModalPresenter`
            - Extends of FromGroup and use the created form interface to as type form form Group