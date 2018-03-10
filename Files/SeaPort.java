/**
 * SeaPort.java - Class for <code>SeaPort</code> objects
 * Begun 01/15/18
 * @author Andrew Eissen
 */

//package project4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * This class represents ports in the world, containing a series of <code>Thing</code> subclass
 * <code>ArrayList</code>s that hold docks, queued ships, all ships, and persons present at the
 * location.
 * <br />
 * <br />
 * A number of <code>synchronized</code> methods for use the in the processing of Project 3's
 * required <code>Job</code> threads were added for use in the final two projects of the class.
 * The new additions basically assist in returning resources to the jockeying <code>Job</code>
 * threads for use in completing their tasks. Only one thread at a time may access the
 * <code>Person</code> worker contents contained within this class's <code>ArrayList</code>s.
 * <br />
 * <br />
 * Class extends <code>Thing</code>
 * @see project4.Thing
 * @author Andrew Eissen
 */
final class SeaPort extends Thing {

    // Rubric-required <code>ArrayList</code>s
    private ArrayList<Dock> docks;
    private ArrayList<Ship> que, ships;
    private ArrayList<Person> persons;

    // Divide Persons by skill, where name of skill is key
    private HashMap<String, ResourcePool> resourcePools;

    /**
     * Parameterized constructor
     * @param scannerContents - Contents of <code>.txt</code> file
     */
    protected SeaPort(Scanner scannerContents) {
        super(scannerContents);
        this.setDocks(new ArrayList<>());
        this.setQue(new ArrayList<>());
        this.setShips(new ArrayList<>());
        this.setPersons(new ArrayList<>());
        this.setResourcePools(new HashMap<>());
    }

    // Setters

    /**
     * Setter for <code>docks</code>
     * @param docks <code>ArrayList</code>
     * @return void
     */
    private void setDocks(ArrayList<Dock> docks) {
        this.docks = docks;
    }

    /**
     * Setter for <code>que</code>
     * @param que <code>ArrayList</code>
     * @return void
     */
    private void setQue(ArrayList<Ship> que) {
        this.que = que;
    }

    /**
     * Setter for <code>ships</code>
     * @param ships <code>ArrayList</code>
     * @return void
     */
    private void setShips(ArrayList<Ship> ships) {
        this.ships = ships;
    }

    /**
     * Setter for <code>persons</code>
     * @param persons <code>ArrayList</code>
     * @return void
     */
    private void setPersons(ArrayList<Person> persons) {
        this.persons = persons;
    }

    /**
     * Setter for <code>resourcePools</code>
     * @param resourcePools <code>HashMap</code>
     * @return void
     */
    private void setResourcePools(HashMap<String, ResourcePool> resourcePools) {
        this.resourcePools = resourcePools;
    }

    // Getters

    /**
     * Getter for <code>docks</code>
     * @return <code>this.dock</code>
     */
    protected ArrayList<Dock> getDocks() {
        return this.docks;
    }

    /**
     * Getter for <code>que</code>
     * @return <code>this.que</code>
     */
    protected ArrayList<Ship> getQue() {
        return this.que;
    }

    /**
     * Getter for <code>ships</code>
     * @return <code>this.ships</code>
     */
    protected ArrayList<Ship> getShips() {
        return this.ships;
    }

    /**
     * Getter for <code>persons</code>
     * @return <code>this.persons</code>
     */
    protected ArrayList<Person> getPersons() {
        return this.persons;
    }

    /**
     * Getter for <code>resourcePools</code>
     * @return <code>this.resourcePools</code>
     */
    protected HashMap<String, ResourcePool> getResourcePools() {
        return this.resourcePools;
    }

    // Utility methods

    /**
     * This method assembles workers from its <code>ArrayList</code> of <code>Person</code>s,
     * <code>this.persons</code>, that match the skills required by the <code>Job</code>s related to
     * <code>Ship</code>s moored at one of this port's <code>Dock</code>s. Those that match the
     * requirements are flagged as working and passed to the job in question for use therein.
     * <br />
     * <br />
     * Originally, in an attempt to realistically demonstrate the movement of workers from ships
     * back to the worker pool, an <code>Iterator</code> was employed to actually physically remove
     * matching workers from <code>this.persons</code> and move them into the job's
     * <code>Job.workers</code> <code>ArrayList</code>, after which time the entries were readded by
     * <code>this.returnResources</code>. This in some ways simplified the searches for needed
     * entries, as the reduced contents of <code>this.persons</code> reflected the fact that the
     * missing <code>Person</code> entries were off undertaking jobs elsewhere were thus unavailable
     * for use on other jobs.
     * <br />
     * <br />
     * However, the author considered that this method/process, though reflective of reality in some
     * ways, was particularly taxing, requiring constant <code>ArrayList</code> manipulations that
     * could be more readily achieved through the use of a <code>boolean</code> flag attached to
     * each <code>Person</code> instance, namely <code>Person.isWorking</code> Furthermore, this
     * approach was similarly employed in the pseudo-code attached to the Project 3 rubric.
     * <br />
     * <br />
     * The method accepts a reference to the <code>Job</code> invoking this method to gain easier
     * access to the assorted <code>Job</code> attributes and methods that are required for this
     * method.
     *
     * @param job <code>Job</code>, reference to the job in question for easier access to methods
     * @return candidates <code>Person</code> <code>ArrayList</code>
     */
    protected synchronized ArrayList<Person> getResources(Job job) {

        // Declarations
        ResourcePool skillGroup;
        ArrayList<Person> candidates;
        boolean areAllRequirementsMet;
        String workerLogLine;
        Person worker;
        HashMap<String, Integer> mapOfNeededSkills;

        // Definitions
        candidates = new ArrayList<>();
        areAllRequirementsMet = true;
        workerLogLine = "";
        mapOfNeededSkills = new HashMap<>();

        /**
         * Implementation suggested <a href="https://stackoverflow.com/questions/81346">here</a> as
         * an ideal Java 8 solution. Basically, to more easily keep track of duplicate skills needed
         * (i.e. two cooks for a job), we create a <code>HashMap</code> entry of that skill to
         * <code>mapOfNeededSkills</code> where the value is the number of workers with that skill
         * that are required to complete the job.
         */
        job.getRequirements().forEach((String skill) -> {
            mapOfNeededSkills.merge(skill, 1, Integer::sum);
        });

        outerLoop:
        for (String skill : job.getRequirements()) {

            // Grab the resource pool possessing all the workers who have this skill in the port
            skillGroup = this.getResourcePools().get(skill);

            // If no workers exist in the port with this specific skill...
            if (skillGroup == null) {
                job.getStatusLog().append("No qualified workers found for " + job.getName()
                    + " (" + job.getParentShip().getName() + ")\n");

                // Release the chopstick, Socrates
                this.returnResources(candidates);
                job.endJob();
                return new ArrayList<>();

            // If the total number of people with this skill is smaller than the needed number...
            } else if (skillGroup.getPersonsInPool().size() < mapOfNeededSkills.get(skill)) {
                job.getStatusLog().append("Not enough qualified workers found for " + job.getName()
                    + " (" + job.getParentShip().getName() + ")\n");

                // Gimme the fork, Epicurus
                this.returnResources(candidates);
                job.endJob();
                return new ArrayList<>();

            // Otherwise...
            } else {

                // For all workers with the required skill
                for (Person person : skillGroup.getPersonsInPool()) {

                    // If this individual is not employed
                    if (!person.getIsWorking()) {
                        skillGroup.reservePerson(person);
                        candidates.add(person);
                        continue outerLoop;
                    }
                }

                // If no available workers are present, we have to keep waiting
                areAllRequirementsMet = false;
                break;
            }
        } // end outerLoop

        // Basically a case of logical conjunction; we only return workers if all cases are true
        if (areAllRequirementsMet) {
            workerLogLine += job.getName() + " (" + job.getParentShip().getName() + ") reserving";

            for (int i = 0; i < candidates.size(); i++) {
                worker = candidates.get(i);

                if (i == 0) {
                    workerLogLine += " ";
                } else if (i < candidates.size() - 1) {
                    workerLogLine += ", ";
                } else {
                    workerLogLine += " & ";
                }

                workerLogLine += worker.getName();
            }
            job.getStatusLog().append(workerLogLine + "\n");

            return candidates;
        } else {

            /**
             * Like the dining philosophers, job cannot be permitted to hold on to <s>chopsticks</s>
             * workers in preparation for gaining any remaining needed workers. If we don't have
             * all requirements met in the form of available workers, we return the worker to the
             * pool and return with <code>null</code>. The author shamefully admits that he failed
             * to realize this fact for far longer than he cares to admit, and that many tests were
             * run before he realized the associated bug.
             */
            this.returnResources(candidates);
            return null;
        }
    }

    /**
     * This method is used by both the <code>SeaPort</code> and <code>Job</code> classes to
     * symbolically return workers to their respective port resource pools at the conclusion of a
     * <code>Job</code> thread. Though before the method employed <code>ArrayList.addAll()</code>
     * to return the formerly removed <code>SeaPort.persons</code> entries to the listing, the
     * current incarnation more easily sets the <code>boolean</code> flag
     * <code>Person.isWorking</code> to false.
     * <br />
     * <br />
     * As per Project 4, the method now calls a new method, <code>ResourcePool.returnPerson</code>
     * that handles the setting of the <code>Person.isWorking</code> flag and managing the text of
     * the appropriate GUI label to indicate the number of available workers of this skill.
     *
     * @param resources <code>ArrayList</code> of <code>Person</code>s, used on previous job
     * @return void
     */
    protected synchronized void returnResources(ArrayList<Person> resources) {
        resources.forEach((Person worker) -> {
            this.getResourcePools().get(worker.getSkill()).returnPerson(worker);
        });
    }

    /**
     * This method simply takes the complete contents of <code>SeaPort.persons</code>, the port's
     * listing of all dock workers, and divides its contents based upon each worker's occupation
     * (value of <code>Person.skill</code>). Each person is added to a newly-created
     * <code>ResourcePool</code> containing fields and methods related to that skill, with said new
     * class instances being created if not already existing. This definitely makes it easier to
     * check if jobs demand a skill that no workers possess; if there is no matching resource pool
     * in the port, no workers have the skill because the pool was never created by this method.
     *
     * @see project4.ResourcePool
     * @return void
     */
    protected void divideWorkersBySkill() {
        ResourcePool myResourcePool;

        for (Person person : this.getPersons()) {
            myResourcePool = this.getResourcePools().get(person.getSkill());

            // Create the pool if no other workers have yet been discovered with this skill
            if (myResourcePool == null) {
                myResourcePool = new ResourcePool(new ArrayList<>(), person.getSkill(),
                    this.getName());
                this.getResourcePools().put(person.getSkill(), myResourcePool);
            }

            myResourcePool.addPerson(person);
        }
    }

    // Overridden methods

    /**
     * @inheritdoc
     * @return stringOutput <code>String</code>
     */
    @Override
    public String toString() {
        String stringOutput;

        // A near-identical implementation of the method as denoted in the rubric
        stringOutput = "\n\nSeaPort: " + super.toString();
        for (Dock dock: this.getDocks()) {
            stringOutput += "\n> " + dock.toString();
        }

        stringOutput += "\n\n --- List of all ships in que:";
        for (Ship shipQue: this.getQue()) {
            stringOutput += "\n> " + shipQue.toString();
        }

        // Since the above output displays ship-related details, this one is just a quick summary
        stringOutput += "\n\n --- List of all ships:";
        for (Ship shipAll: this.getShips()) {
            stringOutput += "\n> " + shipAll.getName() + " " + shipAll.getIndex() + " ("
                + shipAll.getClass().getSimpleName() + ")";
        }

        stringOutput += "\n\n --- List of all persons:";
        for (Person person: this.getPersons()) {
            stringOutput += "\n> " + person.toString();
        }

        return stringOutput;
    }
}